package org.alberto97.ouilookup.ui.search

import android.app.Application
import androidx.lifecycle.*
import androidx.work.ExperimentalExpeditedWork
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.alberto97.ouilookup.repository.IOuiRepository
import org.alberto97.ouilookup.repository.SearchType
import org.alberto97.ouilookup.workers.DownloadWorker
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ExperimentalExpeditedWork
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val app: Application,
    private val repository: IOuiRepository
) : ViewModel() {

    private val _text = MutableStateFlow("")
    val text = _text.asStateFlow()
    private val _filter = MutableStateFlow(0)
    val filter = _filter.asStateFlow()

    val list = combine(_text, _filter) { text, filter ->
        val param = if (filter > 0)
            SearchType.Organization
        else
            SearchType.Address

        Pair(text, param)
    }.flatMapLatest {
        repository.getData(it.first, it.second)
    }

    val searchPlaceholder = _filter.map { filter ->
        if (filter > 0)
            "Search organization"
        else
            "Search MAC address"
    }

    init {
        updateDb()
    }

    private fun updateDb() {
        val downloadWork = OneTimeWorkRequestBuilder<DownloadWorker>().apply {
            setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
        }
        WorkManager.getInstance(app).enqueue(downloadWork.build())
    }

    fun onTextChange(text: String) {
        _text.value = text
    }

    fun onFilterChange(option: Int) {
        _filter.value = option
        _text.value = ""
    }
}