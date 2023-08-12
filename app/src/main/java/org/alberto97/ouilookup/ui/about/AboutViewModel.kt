package org.alberto97.ouilookup.ui.about

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.format.DateUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.alberto97.ouilookup.BuildConfig
import org.alberto97.ouilookup.MainApplication
import org.alberto97.ouilookup.repository.IOuiRepository
import org.alberto97.ouilookup.repository.ISettingsRepository
import org.alberto97.ouilookup.repository.OuiRepository
import org.alberto97.ouilookup.repository.SettingsRepository
import org.alberto97.ouilookup.tools.AppStoreUtils
import org.alberto97.ouilookup.tools.IAppStoreUtils

enum class AppStore {
    Oss,
    PlayStore
}

class AboutViewModel(
    private val app: Application = MainApplication.instance,
    ouiRepo: IOuiRepository = OuiRepository(),
    private val appStoreUtils: IAppStoreUtils = AppStoreUtils(),
    private val settings: ISettingsRepository = SettingsRepository()
) : ViewModel() {

    val availableAppStore = if (appStoreUtils.isPlayStoreAvailable()) AppStore.PlayStore else AppStore.Oss
    val appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
    val dbVersion = ouiRepo.getLastDbUpdate().map { millis -> formatLastDbUpdateDate(millis) }

    val supportedDynamicTheme = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    lateinit var useDynamicTheme: StateFlow<Boolean>

    init {
        viewModelScope.launch {
            init()
        }
    }

    private suspend fun init() {
        useDynamicTheme = settings.getUseDynamicTheme().stateIn(scope = viewModelScope)
    }

    private fun formatLastDbUpdateDate(millis: Long): String {
        val options = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_YEAR
        return DateUtils.formatDateTime(app, millis, options)
    }

    fun openRepository() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://github.com/Alberto97/OUILookup")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        app.startActivity(intent)
    }

    fun openAppStoreForReview() {
        appStoreUtils.openForReview()
    }

    fun openOtherApps() {
        appStoreUtils.openOtherApps()
    }

    fun toggleUseDynamicTheme() {
        viewModelScope.launch {
            settings.setUseDynamicTheme(!useDynamicTheme.value)
        }
    }
}