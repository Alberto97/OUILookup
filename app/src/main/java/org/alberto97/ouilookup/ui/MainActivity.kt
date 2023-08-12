package org.alberto97.ouilookup.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alberto97.ouilookup.repository.SettingsRepository
import org.alberto97.ouilookup.tools.FeedbackManager

class MainActivity : ComponentActivity() {
    private val settings = SettingsRepository()
    private val feedback = FeedbackManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            persistFirstLaunchInstant()
            askForReview()
        }

        setContent {
            OuiLookupApp(settings)
        }
    }

    private suspend fun persistFirstLaunchInstant() = withContext(Dispatchers.IO) {
        val firstLaunch = settings.getFirstLaunchInstant().first()
        if (firstLaunch == 0L) {
            val now = System.currentTimeMillis()
            settings.setFirstLaunchInstant(now)
        }
    }

    private suspend fun askForReview() {
        if (feedback.shouldAskForReview())
            feedback.openReviewWindow(this@MainActivity)
    }
}