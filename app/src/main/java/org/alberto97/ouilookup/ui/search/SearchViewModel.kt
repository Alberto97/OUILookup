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
import org.alberto97.ouilookup.tools.OctetTool
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val app: Application,
    private val repository: IOuiRepository,
    private val updateManager: IUpdateManager
) : ViewModel() {

    private val _text = MutableStateFlow("")
    val text = _text.asStateFlow()

    val list = _text.map { text -> repository.get(text) }

    val placeholder = combine(updateManager.pendingUpdate, _text) { workRunning, text ->
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
        if (isSearchable && _text.value.isEmpty()) {
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
            updateManager.shouldUpdate()
        }
    }

    fun onTextChange(text: String) {
        _text.value = text
    }
}