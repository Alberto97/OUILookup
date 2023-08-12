package org.alberto97.ouilookup.tools

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import org.alberto97.ouilookup.MainApplication

interface IAppConnectivityManager {
    fun isConnected(): Boolean
}

class AppConnectivityManager(
    private val context: Context = MainApplication.instance
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