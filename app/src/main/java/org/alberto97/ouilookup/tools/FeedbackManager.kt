package org.alberto97.ouilookup.tools

import kotlinx.coroutines.flow.first
import org.alberto97.ouilookup.repository.IFeedbackRepository
import org.alberto97.ouilookup.repository.ISettingsRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.DurationUnit
import kotlin.time.toDuration

interface IFeedbackManager {
    suspend fun shouldAskForReview(): Boolean
    suspend fun updateLastRequestInstant()
    suspend fun doNotAskAgain()
}

@Singleton
class FeedbackManager @Inject constructor(
    private val feedbackRepository: IFeedbackRepository,
    private val settingsRepository: ISettingsRepository,
    private val appStoreUtils: IAppStoreUtils
) : IFeedbackManager {
    companion object {
        const val DAYS_BETWEEN_REQUESTS = 2
    }

    override suspend fun doNotAskAgain() {
        feedbackRepository.setShowAgain(false)
    }

    override suspend fun updateLastRequestInstant() {
        val instant = System.currentTimeMillis()
        feedbackRepository.setLastRequestInstant(instant)
    }

    override suspend fun shouldAskForReview(): Boolean {
        val lastRequestMillis = feedbackRepository.getLastRequestInstant().first()
        val firstLaunchMillis = settingsRepository.getFirstLaunchInstant().first()
        val showAgain = feedbackRepository.getShowAgain().first()
        val playStoreAvailable = appStoreUtils.isPlayStoreAvailable()

        if (!showAgain || !playStoreAvailable)  return false

        val durationFirst = (System.currentTimeMillis() - firstLaunchMillis).toDuration(DurationUnit.MILLISECONDS)
        if (durationFirst.inWholeDays < DAYS_BETWEEN_REQUESTS) return false

        val duration = (System.currentTimeMillis() - lastRequestMillis).toDuration(DurationUnit.MILLISECONDS)
        return duration.inWholeDays >= DAYS_BETWEEN_REQUESTS
    }
}
