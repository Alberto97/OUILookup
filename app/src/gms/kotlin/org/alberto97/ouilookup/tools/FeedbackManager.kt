package org.alberto97.ouilookup.tools

import android.app.Activity
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.flow.first
import org.alberto97.ouilookup.repository.FeedbackRepository
import org.alberto97.ouilookup.repository.IFeedbackRepository
import org.alberto97.ouilookup.repository.ISettingsRepository
import org.alberto97.ouilookup.repository.SettingsRepository
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class FeedbackManager(
    private val feedbackRepository: IFeedbackRepository = FeedbackRepository(),
    private val settingsRepository: ISettingsRepository = SettingsRepository()
) : IFeedbackManager {
    companion object {
        const val DAYS_BETWEEN_REQUESTS = 2
    }

    private suspend fun updateLastRequestInstant() {
        val instant = System.currentTimeMillis()
        feedbackRepository.setLastRequestInstant(instant)
    }

    override suspend fun shouldAskForReview(): Boolean {
        val lastRequestMillis = feedbackRepository.getLastRequestInstant().first()
        val firstLaunchMillis = settingsRepository.getFirstLaunchInstant().first()

        val durationFirst = (System.currentTimeMillis() - firstLaunchMillis).toDuration(DurationUnit.MILLISECONDS)
        if (durationFirst.inWholeDays < DAYS_BETWEEN_REQUESTS) return false

        val duration = (System.currentTimeMillis() - lastRequestMillis).toDuration(DurationUnit.MILLISECONDS)
        return duration.inWholeDays >= DAYS_BETWEEN_REQUESTS
    }

    override suspend fun openReviewWindow(activity: Activity) {
        val manager = ReviewManagerFactory.create(activity)
        val reviewInfo = manager.requestReview()
        manager.launchReview(activity, reviewInfo)
        updateLastRequestInstant()
    }
}
