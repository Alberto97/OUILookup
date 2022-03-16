package org.alberto97.ouilookup.ui.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.alberto97.ouilookup.tools.AppStoreUtils
import org.alberto97.ouilookup.tools.FeedbackManager
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val appStoreUtils: AppStoreUtils,
    private val feedbackManager: FeedbackManager
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            feedbackManager.updateLastRequestInstant()
        }
    }

    fun doNotShowAgain() {
        viewModelScope.launch(Dispatchers.IO) {
            feedbackManager.doNotAskAgain()
        }
    }

    fun openAppStoreForReview() {
        appStoreUtils.openForReview()
    }
}
