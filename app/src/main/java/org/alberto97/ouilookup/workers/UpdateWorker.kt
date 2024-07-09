package org.alberto97.ouilookup.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.alberto97.ouilookup.R
import org.alberto97.ouilookup.repository.IOuiRepository

@HiltWorker
class UpdateWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: IOuiRepository
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val LOG_TAG = "UpdateWorker"
    }

    private val notificationManager = NotificationManagerCompat.from(appContext)
    private val channelId = "oui_updates"

    override suspend fun doWork(): Result {
        try {
            repository.updateFromIEEE()
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.stackTraceToString())
            return Result.failure()
        }

        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return createForegroundInfo()
    }

    /**
     * Create ForegroundInfo required to run a Worker in a foreground service.
     */
    private fun createForegroundInfo(): ForegroundInfo {
        // Use a different id for each Notification.
        val notificationId = 1
        return ForegroundInfo(notificationId, createNotification())
    }

    /**
     * Create the notification and required channel (O+) for running work
     * in a foreground service.
     */
    private fun createNotification(): Notification {

        val title = appContext.getString(R.string.oui_update_notification_title)
        val builder = NotificationCompat.Builder(appContext, channelId)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_stat_developer_board)
            .setOngoing(true)
            .setProgress(100, 0, true)

        val channelName = appContext.getString(R.string.oui_update_notification_channel)
        createNotificationChannel(channelId, channelName).also {
            builder.setChannelId(it.id)
        }

        return builder.build()
    }

    /**
     * Create the required notification channel for O+ devices.
     */
    @Suppress("SameParameterValue")
    private fun createNotificationChannel(
        id: String,
        name: String
    ): NotificationChannel {
        return NotificationChannel(
            id, name, NotificationManager.IMPORTANCE_LOW
        ).also { channel ->
            notificationManager.createNotificationChannel(channel)
        }
    }
}