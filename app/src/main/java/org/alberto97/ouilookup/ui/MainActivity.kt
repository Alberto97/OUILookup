package org.alberto97.ouilookup.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alberto97.ouilookup.repository.ISettingsRepository
import org.alberto97.ouilookup.tools.IFeedbackManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settings: ISettingsRepository

    @Inject
    lateinit var feedback: IFeedbackManager

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