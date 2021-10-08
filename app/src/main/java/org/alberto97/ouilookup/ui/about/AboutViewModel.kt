package org.alberto97.ouilookup.ui.about

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.text.format.DateUtils
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import org.alberto97.ouilookup.BuildConfig
import org.alberto97.ouilookup.repository.IOuiRepository
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(private val app: Application, ouiRepo: IOuiRepository) : ViewModel() {

    val appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
    val dbVersion = ouiRepo.getLastDbUpdate().map { millis -> formatLastDbUpdateDate(millis) }

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
}