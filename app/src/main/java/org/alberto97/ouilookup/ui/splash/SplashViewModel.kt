package org.alberto97.ouilookup.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alberto97.ouilookup.repository.IOuiRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: IOuiRepository) : ViewModel() {

    private val _isUpdating = MutableStateFlow(false)
    val isUpdating = _isUpdating.asStateFlow()

    private val _navigate = MutableStateFlow(false)
    val navigate = _navigate.asStateFlow()


    init {
        updateDb()
    }

    private fun updateDb() {
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.dbNeedsUpdate()) {
                withContext(Dispatchers.Main) { _isUpdating.value = true }
                repository.updateIfOldOrEmpty()
            }
            delay(1000L)
            withContext(Dispatchers.Main) { _navigate.value = true }
        }
    }
}