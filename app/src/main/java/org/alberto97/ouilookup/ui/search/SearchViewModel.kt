package org.alberto97.ouilookup.ui.search

import android.app.Application
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alberto97.ouilookup.repository.IOuiRepository
import org.alberto97.ouilookup.workers.DownloadWorker
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ExperimentalExpeditedWork
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val app: Application,
    private val repository: IOuiRepository
) : ViewModel() {

    private val downloadWorkRunning = MutableStateFlow(false)

    private val _text = MutableStateFlow("")
    val text = _text.asStateFlow()

    val list = _text.flatMapLatest { text ->
        if (text.isEmpty())
            repository.getAll()
        else
            repository.get(text)
    }

    val updatingDb = combine(downloadWorkRunning, _text) { workRunning, text ->
        text.isEmpty() && workRunning
    }

    init {
        shouldUpdateDb()
    }

    private fun shouldUpdateDb() {
        viewModelScope.launch {
            val needsUpdate = withContext(Dispatchers.IO) {
                repository.dbNeedsUpdate()
            }
            if (needsUpdate)
                updateDb()
        }
    }

    private fun updateDb() {
        val downloadWork = OneTimeWorkRequestBuilder<DownloadWorker>().apply {
            setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
        }.build()

        WorkManager.getInstance(app).also {
            it.enqueue(downloadWork)
            it.getWorkInfoByIdLiveData(downloadWork.id)
                .observeForever(object : Observer<WorkInfo> {
                    override fun onChanged(workInfo : WorkInfo) {
                        if (workInfo.state == WorkInfo.State.RUNNING)
                            downloadWorkRunning.value = true

                        if (workInfo.state.isFinished) {
                            downloadWorkRunning.value = false
                            it.getWorkInfoByIdLiveData(downloadWork.id)
                                .removeObserver(this)
                        }
                    }
                })
        }
    }

    fun onTextChange(text: String) {
        _text.value = text
    }
}