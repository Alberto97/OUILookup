package org.alberto97.ouilookup.tools

import android.app.Activity


interface IFeedbackManager {
    suspend fun shouldAskForReview(): Boolean
    suspend fun openReviewWindow(activity: Activity)
}
