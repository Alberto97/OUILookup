package org.alberto97.ouilookup.ui.search

import android.app.Application
import android.content.ClipboardManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alberto97.ouilookup.repository.IOuiRepository
import org.alberto97.ouilookup.tools.IUpdateManager
import org.alberto97.ouilookup.tools.OctetTool
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val app: Application,
    private val repository: IOuiRepository,
    private val updateManager: IUpdateManager,
    private val workManager: WorkManager
) : ViewModel() {

    private val updateWorkRunning = MutableStateFlow(false)

    private val _text = MutableStateFlow("")
    val text = _text.asStateFlow()

    val list = _text.map { text -> repository.get(text) }

    val placeholder = combine(updateWorkRunning, _text) { workRunning, text ->
        when {
            text.isEmpty() && workRunning -> UiSearchPlaceholder.Updating
            text.isEmpty() -> UiSearchPlaceholder.Instructions
            list.first().isEmpty() -> UiSearchPlaceholder.NoResults
            else -> null
        }
    }

    init {
        shouldUpdateDb()
    }

    fun checkClipboard() {
        val clip = getClipboard()?.toString() ?: return
        val isSearchable = OctetTool.isOui(clip) || OctetTool.isMacAddress(clip)
        if (isSearchable) {
            _text.value = clip
        }
    }

    private fun getClipboard(): CharSequence? {
        val clipboardManager = ContextCompat.getSystemService(app, ClipboardManager::class.java)
        val data = clipboardManager?.primaryClip?.getItemAt(0)
        return data?.text
    }

    private fun shouldUpdateDb() {
        viewModelScope.launch {
            val workRequest = withContext(Dispatchers.IO) {
                updateManager.shouldEnqueueUpdate()
            } ?: return@launch

            withContext(Dispatchers.Main) {
                workManager
                    .getWorkInfoByIdLiveData(workRequest.id)
                    .notifyUpdateWorkStateChange()
            }
        }
    }

    private fun LiveData<WorkInfo>.notifyUpdateWorkStateChange() {
        observeForever(object : Observer<WorkInfo> {
            override fun onChanged(workInfo: WorkInfo) {
                if (workInfo.state == WorkInfo.State.RUNNING)
                    updateWorkRunning.value = true

                if (workInfo.state.isFinished) {
                    updateWorkRunning.value = false
                    removeObserver(this)
                }
            }
        })
    }

    fun onTextChange(text: String) {
        _text.value = text
    }
}