package org.alberto97.ouilookup.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alberto97.ouilookup.R
import org.alberto97.ouilookup.repository.IOuiRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: IOuiRepository
) : ViewModel() {
    private val notificationManager = NotificationManagerCompat.from(context)
    private val notificationId = 1
    private val notificationChannel = "oui_updates"

    init {
        //updateDb()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channelName = "Canale di Prova" //context.getString(R.string.bts_update_notification_channel)
        val channel = NotificationChannel(notificationChannel, channelName, NotificationManager.IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }

    fun updateDb(forceUpdate: Boolean = false) {
        createChannel()
        val title = "Aggiornamento OUI"//context.getString(R.string.bts_update_notification_title)
        val notificationBuilder = NotificationCompat.Builder(context, notificationChannel)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setOngoing(true)
            .setProgress(100, 0, true)

        val notification: Notification = notificationBuilder.build()
        notificationManager.notify(notificationId, notification)

        viewModelScope.launch(Dispatchers.IO) {
            if (forceUpdate) {
                //repository.updateBts()
            } else {
                repository.updateIfOldOrEmpty()
            }
            withContext(Dispatchers.Main) {
                notificationManager.cancel(notificationId)
            }
        }
    }
}