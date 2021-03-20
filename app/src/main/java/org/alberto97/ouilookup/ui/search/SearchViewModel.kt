package org.alberto97.ouilookup.ui.search

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import org.alberto97.ouilookup.tools.DoubleTrigger
import org.alberto97.ouilookup.repository.IOuiRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: IOuiRepository) : ViewModel() {

    private val _text = MutableLiveData("")
    val text: LiveData<String> = _text
    private val _option = MutableLiveData("Address")
    val option: LiveData<String> = _option

    val list = Transformations.switchMap(DoubleTrigger(_text, _option)) {
        repository.getData(it.first, it.second).map { value ->
            value.map { item -> Pair(item.orgName, item.oui)}
        }.asLiveData()
    }

    fun onTextChange(text: String) {
        _text.value = text
    }

    fun onTypeChange(option: String) {
        _option.value = option
        _text.value = ""
    }
}