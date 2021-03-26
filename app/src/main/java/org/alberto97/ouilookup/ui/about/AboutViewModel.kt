package org.alberto97.ouilookup.ui.about

import android.content.Context
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import org.alberto97.ouilookup.BuildConfig
import org.alberto97.ouilookup.repository.IOuiRepository
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(@ApplicationContext context: Context, ouiRepo: IOuiRepository) : ViewModel() {

    val appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"

    private val _dbVersion = MutableLiveData("")
    val dbVersion: LiveData<String> = _dbVersion

    init {
        val options = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_YEAR
        _dbVersion.value = DateUtils.formatDateTime(context, ouiRepo.getLastDbUpdate(), options)
    }
}