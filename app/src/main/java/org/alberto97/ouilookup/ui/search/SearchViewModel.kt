package org.alberto97.ouilookup.ui.search

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.alberto97.ouilookup.repository.IOuiRepository
import org.alberto97.ouilookup.repository.SearchType
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: IOuiRepository) : ViewModel() {

    private val _text = MutableStateFlow("")
    val text = _text.asStateFlow()
    private val _filter = MutableStateFlow(0)
    val filter = _filter.asStateFlow()

    val list = combine(_text, _filter) {text, filter ->
        val param = if (filter > 0)
            SearchType.Organization
        else
            SearchType.Address

        Pair(text, param)
    }.flatMapLatest {
        repository.getData(it.first, it.second)
    }

    fun onTextChange(text: String) {
        _text.value = text
    }

    fun onFilterChange(option: Int) {
        _filter.value = option
        _text.value = ""
    }
}