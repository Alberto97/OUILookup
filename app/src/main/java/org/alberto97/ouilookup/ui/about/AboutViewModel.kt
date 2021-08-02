package org.alberto97.ouilookup.ui.about

import android.content.Context
import android.text.format.DateUtils
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.alberto97.ouilookup.BuildConfig
import org.alberto97.ouilookup.repository.IOuiRepository
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(@ApplicationContext context: Context, ouiRepo: IOuiRepository) : ViewModel() {

    val appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"

    private val _dbVersion = MutableStateFlow("")
    val dbVersion = _dbVersion.asStateFlow()

    init {
        val options = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_YEAR
        _dbVersion.value = DateUtils.formatDateTime(context, ouiRepo.getLastDbUpdate(), options)
    }
}