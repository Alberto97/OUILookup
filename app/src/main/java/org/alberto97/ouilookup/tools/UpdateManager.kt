package org.alberto97.ouilookup.tools

import android.content.Context
import android.util.Log
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.alberto97.ouilookup.Extensions.readRawTextFile
import org.alberto97.ouilookup.R
import org.alberto97.ouilookup.repository.IOuiRepository
import org.alberto97.ouilookup.repository.ISettingsRepository
import org.alberto97.ouilookup.workers.UpdateWorker
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.DurationUnit
import kotlin.time.toDuration

interface IUpdateManager {
    val pendingUpdate: StateFlow<Boolean>
    suspend fun shouldUpdate()
}

@Singleton
class UpdateManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: IOuiRepository,
    private val settings: ISettingsRepository,
    private val workManager: WorkManager
) : IUpdateManager {
    companion object {
        const val WORK_NAME = "updateDb"
    }

    private val _pendingUpdate = MutableStateFlow(false)
    override val pendingUpdate = _pendingUpdate.asStateFlow()

    private fun buildUpdateWorkRequest(): OneTimeWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        return OneTimeWorkRequestBuilder<UpdateWorker>()
            .setConstraints(constraints)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
    }

    /**
     * Start an update from the IEEE
     */
    private suspend fun remoteUpdate() = withContext(Dispatchers.Main) {
        Log.d("UpdateManager", "Start remote update")
        val workRequest = buildUpdateWorkRequest()

        workManager.enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.REPLACE, workRequest)
        workManager.getWorkInfoByIdFlow(workRequest.id).collect { value ->
            _pendingUpdate.value = !value.state.isFinished
        }
    }

    /**
     * Start an update from the bundled CSV
     */
    private suspend fun localUpdate() {
        Log.d("UpdateManager", "Start local update")
        _pendingUpdate.value = true
        repository.updateFromCsv()
        _pendingUpdate.value = false
    }

    /**
     * Update from both the CSV and IEEE
     */
    private suspend fun bothUpdates() {
        Log.d("UpdateManager", "Both updates")
        localUpdate()
        remoteUpdate()
    }

    override suspend fun shouldUpdate() {
        val bundledUpdateMillis = context.resources.readRawTextFile(R.raw.oui_date_millis).toLong()
        val lastUpdateMillis = settings.getLastDbUpdate().first()
        when (UpdatePolicyManager.getUpdatePolicy(bundledUpdateMillis, lastUpdateMillis)) {
            UpdatePolicy.Local -> localUpdate()
            UpdatePolicy.Remote -> remoteUpdate()
            UpdatePolicy.Both -> bothUpdates()
            UpdatePolicy.None -> {}
        }
    }

}

enum class UpdatePolicy {
    None,
    Local,
    Remote,
    Both
}

object UpdatePolicyManager {
    fun getUpdatePolicy(bundledUpdateMillis: Long, lastUpdateMillis: Long): UpdatePolicy {
        if (bundledUpdateMillis > lastUpdateMillis) {
            return if (isOutdated(bundledUpdateMillis))
                UpdatePolicy.Both
            else
                UpdatePolicy.Local
        }

        if (isOutdated(lastUpdateMillis))
            return UpdatePolicy.Remote

        return UpdatePolicy.None
    }

    fun isOutdated(lastUpdateMillis: Long): Boolean {
        // Don't update until at least a month has passed since the last data fetch
        val duration = (System.currentTimeMillis() - lastUpdateMillis).toDuration(DurationUnit.MILLISECONDS)
        return duration.inWholeDays >= 30
    }
}
