package org.alberto97.ouilookup.ui.search

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import org.alberto97.ouilookup.repository.IOuiRepository
import org.alberto97.ouilookup.repository.SearchType
import org.alberto97.ouilookup.tools.DoubleTrigger
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: IOuiRepository) : ViewModel() {

    private val _text = MutableLiveData("")
    val text: LiveData<String> = _text
    private val _filter = MutableLiveData(0)
    val filter: LiveData<Int> = _filter

    val list = Transformations.switchMap(DoubleTrigger(_text, _filter)) {
        val param = if (it.second!! > 0)
            SearchType.Organization
        else
            SearchType.Address

        repository.getData(it.first, param).asLiveData()
    }

    fun onTextChange(text: String) {
        _text.value = text
    }

    fun onFilterChange(option: Int) {
        _filter.value = option
        _text.value = ""
    }
}