package org.alberto97.ouilookup.ui.search

import android.app.Application
import android.content.ClipboardManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.alberto97.ouilookup.repository.IOuiRepository
import org.alberto97.ouilookup.tools.IUpdateManager
import org.alberto97.ouilookup.tools.StringInspector
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val app: Application,
    private val repository: IOuiRepository,
    private val updateManager: IUpdateManager
) : ViewModel() {

    private val _text = MutableStateFlow("")
    val text = _text.asStateFlow()

    private val _bulkLookupList = MutableStateFlow(listOf<String>())
    val bulkLookupList = _bulkLookupList.asStateFlow()

    val list = combine(_text, _bulkLookupList) { text, list ->
        if (text.isNotEmpty())
            repository.search(text)
        else
            repository.getMany(list)
    }

    val placeholder = combine(updateManager.pendingUpdate, _text, _bulkLookupList) { workRunning, text, lookupList ->
        when {
            text.isEmpty() && workRunning -> UiSearchPlaceholder.Updating
            text.isEmpty() && lookupList.isEmpty() -> UiSearchPlaceholder.Instructions
            list.first().isEmpty() -> UiSearchPlaceholder.NoResults
            else -> null
        }
    }

    init {
        shouldUpdateDb()
    }

    fun checkClipboard() {
        if (_text.value.isNotEmpty()) return
        val clip = getClipboard()?.toString() ?: return

        // Check for OUIs/MAC Addresses
        val list = StringInspector.splitForList(clip)
        val counted = StringInspector.countSearchable(list)
        if (counted == 1)
            _text.value = clip
        else if (counted > 1)
            _bulkLookupList.value = list
    }

    private fun getClipboard(): CharSequence? {
        val clipboardManager = ContextCompat.getSystemService(app, ClipboardManager::class.java)
        val data = clipboardManager?.primaryClip?.getItemAt(0)
        return data?.text
    }

    private fun shouldUpdateDb() {
        viewModelScope.launch {
            updateManager.shouldUpdate()
        }
    }

    fun onTextChange(text: String) {
        val list = StringInspector.splitForList(text)
        if (StringInspector.countSearchable(list) > 1) {
            _text.value = ""
            _bulkLookupList.value = list
        } else {
            _text.value = text
            _bulkLookupList.value = listOf()
        }
    }
}