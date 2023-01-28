package org.alberto97.ouilookup.tools

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.alberto97.ouilookup.repository.IFeedbackRepository
import org.alberto97.ouilookup.repository.ISettingsRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.OffsetDateTime

class FeedbackManagerTest {

    private lateinit var feedbackManager: FeedbackManager

    private val feedback: IFeedbackRepository = mockk()
    private val settings: ISettingsRepository = mockk()

    @Before
    fun setup() {
        feedbackManager = FeedbackManager(feedback, settings)
    }

    @Test
    fun testFirstLaunch() = runBlocking {
        every { settings.getFirstLaunchInstant() } returns flowOf(System.currentTimeMillis())
        every { feedback.getLastRequestInstant() } returns flowOf(0)

        val result = feedbackManager.shouldAskForReview()
        assertEquals(false, result)
    }

    @Test
    fun test2DaysAfterFirstLaunch() = runBlocking {
        val now = OffsetDateTime.now()
        val firstLaunch = now.minusDays(FeedbackManager.DAYS_BETWEEN_REQUESTS.toLong()).toInstant().toEpochMilli()
        every {settings.getFirstLaunchInstant() } returns flowOf(firstLaunch)
        every {feedback.getLastRequestInstant() } returns flowOf(0)

        val result = feedbackManager.shouldAskForReview()
        assertEquals(true, result)
    }

    @Test
    fun test2DaysAfterLastRequest() = runBlocking {
        val now = OffsetDateTime.now()
        val lastRequest = now.minusDays(FeedbackManager.DAYS_BETWEEN_REQUESTS.toLong()).toInstant().toEpochMilli()
        val firstLaunch = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, now.offset).toInstant().toEpochMilli()
        every {settings.getFirstLaunchInstant() } returns flowOf(firstLaunch)
        every {feedback.getLastRequestInstant() } returns flowOf(lastRequest)

        val result = feedbackManager.shouldAskForReview()
        assertEquals(true, result)
    }
}
