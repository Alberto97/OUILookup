package org.alberto97.ouilookup.tools

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.alberto97.ouilookup.repository.IFeedbackRepository
import org.alberto97.ouilookup.repository.ISettingsRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.OffsetDateTime

class FeedbackManagerTest {

    private lateinit var feedbackManager: FeedbackManager

    @Mock
    private lateinit var feedback: IFeedbackRepository

    @Mock
    private lateinit var settings: ISettingsRepository

    @Before
    fun setup() {
        val appStoreUtils: IAppStoreUtils = mock {
            on { isPlayStoreAvailable() } doReturn true
        }
        feedback = mock()
        settings = mock()
        feedbackManager = FeedbackManager(feedback, settings, appStoreUtils)
    }

    @Test
    fun testFirstLaunch() = runBlocking {
        whenever(settings.getFirstLaunchInstant()).thenReturn(flowOf(System.currentTimeMillis()))
        whenever(feedback.getLastRequestInstant()).thenReturn(flowOf(0))
        whenever(feedback.getShowAgain()).thenReturn(flowOf(true))

        val result = feedbackManager.shouldAskForReview()
        assertEquals(false, result)
    }

    @Test
    fun test2DaysAfterFirstLaunch() = runBlocking {
        val now = OffsetDateTime.now()
        val firstLaunch = now.minusDays(FeedbackManager.DAYS_BETWEEN_REQUESTS.toLong()).toInstant().toEpochMilli()
        whenever(settings.getFirstLaunchInstant()).thenReturn(flowOf(firstLaunch))
        whenever(feedback.getLastRequestInstant()).thenReturn(flowOf(0))
        whenever(feedback.getShowAgain()).thenReturn(flowOf(true))

        val result = feedbackManager.shouldAskForReview()
        assertEquals(true, result)
    }

    @Test
    fun test2DaysAfterLastRequest() = runBlocking {
        val now = OffsetDateTime.now()
        val lastRequest = now.minusDays(FeedbackManager.DAYS_BETWEEN_REQUESTS.toLong()).toInstant().toEpochMilli()
        val firstLaunch = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, now.offset).toInstant().toEpochMilli()
        whenever(settings.getFirstLaunchInstant()).thenReturn(flowOf(firstLaunch))
        whenever(feedback.getLastRequestInstant()).thenReturn(flowOf(lastRequest))
        whenever(feedback.getShowAgain()).thenReturn(flowOf(true))

        val result = feedbackManager.shouldAskForReview()
        assertEquals(true, result)
    }

    @Test
    fun testNever() = runBlocking {
        val now = OffsetDateTime.now()
        val lastRequest = now.minusDays(FeedbackManager.DAYS_BETWEEN_REQUESTS.toLong()).toInstant().toEpochMilli()
        val firstLaunch = OffsetDateTime.of(2020, 1, 1, 0, 0, 0, 0, now.offset).toInstant().toEpochMilli()
        whenever(settings.getFirstLaunchInstant()).thenReturn(flowOf(firstLaunch))
        whenever(feedback.getLastRequestInstant()).thenReturn(flowOf(lastRequest))
        whenever(feedback.getShowAgain()).thenReturn(flowOf(false))

        val result = feedbackManager.shouldAskForReview()
        assertEquals(false, result)
    }
}
