package org.alberto97.ouilookup.tools

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import org.alberto97.ouilookup.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

interface IAppStoreUtils {
    fun isPlayStoreAvailable(): Boolean
    fun openForReview()
    fun openOtherApps()
}

@Singleton
class AppStoreUtils @Inject constructor(val app: Application) : IAppStoreUtils {
    companion object {
        private const val FDROID_SEARCH_URL = "https://search.f-droid.org/?q="
        private const val FDROID_DEVELOPER_URL = FDROID_SEARCH_URL + "org.alberto97"
        private const val PLAY_STORE_DEVELOPER_ID = "6891464730852554224"
        private const val PLAY_STORE_APP_URL = "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        private const val PLAY_STORE_DEVELOPER_URL = "http://play.google.com/store/apps/dev?id=${PLAY_STORE_DEVELOPER_ID}"
    }

    override fun isPlayStoreAvailable(): Boolean {
        return try {
            PackageManagerCompat.getPackageInfo(app.packageManager, "com.android.vending")
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    override fun openForReview() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(PLAY_STORE_APP_URL)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        app.startActivity(intent)
    }

    override fun openOtherApps() {
        val url = if (isPlayStoreAvailable()) PLAY_STORE_DEVELOPER_URL else FDROID_DEVELOPER_URL
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        app.startActivity(intent)
    }
}
