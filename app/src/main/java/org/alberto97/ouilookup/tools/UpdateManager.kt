package org.alberto97.ouilookup.tools

import androidx.work.*
import kotlinx.coroutines.flow.first
import org.alberto97.ouilookup.db.OuiDao
import org.alberto97.ouilookup.repository.ISettingsRepository
import org.alberto97.ouilookup.workers.UpdateWorker
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.DurationUnit
import kotlin.time.toDuration

interface IUpdateManager {
    suspend fun shouldEnqueueUpdate(): OneTimeWorkRequest?
}

@Singleton
class UpdateManager @Inject constructor(
    private val dao: OuiDao,
    private val settings: ISettingsRepository,
    private val workManager: WorkManager
) : IUpdateManager {
    companion object {
        const val WORK_NAME = "updateDb"
    }

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
     * Enqueue an update from the IEEE
     */
    private fun enqueueUpdate(): OneTimeWorkRequest {
        val workRequest = buildUpdateWorkRequest()

        workManager.enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.REPLACE, workRequest)

        return workRequest
    }

    override suspend fun shouldEnqueueUpdate(): OneTimeWorkRequest? {
        // If db is empty it means it is still being seeded and must not be scheduled another update
        val isEmpty = dao.isEmpty()
        val lastUpdateMillis = settings.getLastDbUpdate().first()
        if (UpdatePolicyManager.isOutdated(isEmpty, lastUpdateMillis))
            return enqueueUpdate()

        return null
    }

}

object UpdatePolicyManager {
    fun isOutdated(emptyDb: Boolean, lastUpdateMillis: Long): Boolean {
        return !emptyDb && needsUpdate(lastUpdateMillis)
    }

    fun needsUpdate(lastUpdateMillis: Long): Boolean {
        // Don't update until at least a month has passed since the last data fetch
        val duration = (System.currentTimeMillis() - lastUpdateMillis).toDuration(DurationUnit.MILLISECONDS)
        return duration.inWholeDays >= 30
    }
}
