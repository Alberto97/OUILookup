package org.alberto97.ouilookup.tools

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface IAppConnectivityManager {
    fun isConnected(): Boolean
}

@Singleton
class AppConnectivityManager @Inject constructor(
    @ApplicationContext private val context: Context
): IAppConnectivityManager {

    override fun isConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            isConnected(cm)
        else
            isConnectedLegacy(cm)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isConnected(cm: ConnectivityManager): Boolean {
        val cap = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
        return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    @Suppress("DEPRECATION")
    private fun isConnectedLegacy(cm: ConnectivityManager): Boolean {
        val networks = cm.allNetworks
        for (n in networks) {
            val nInfo = cm.getNetworkInfo(n)
            if (nInfo != null && nInfo.isConnected) return true
        }

        return false
    }
}