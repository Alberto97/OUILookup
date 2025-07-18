package org.alberto97.ouilookup.tools

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject
import javax.inject.Singleton

interface IAppConnectivityManager {
    fun isConnected(): Boolean
}

@Singleton
class AppConnectivityManager @Inject constructor(
    private val app: Application
): IAppConnectivityManager {

    override fun isConnected(): Boolean {
        val cm = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val cap = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
        return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}