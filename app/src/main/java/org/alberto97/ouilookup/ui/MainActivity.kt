package org.alberto97.ouilookup.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.alberto97.ouilookup.ui.theme.OUILookupTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalCoroutinesApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OUILookupTheme {
                NavGraph()
            }
        }
    }
}