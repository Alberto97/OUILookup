package org.alberto97.ouilookup.tools

import android.app.Activity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FeedbackManager @Inject constructor() : IFeedbackManager {

    override suspend fun shouldAskForReview(): Boolean {
        // Do not ask for review in the FOSS flavor
        return false
    }

    override suspend fun openReviewWindow(activity: Activity) {
        // Noop
    }
}
