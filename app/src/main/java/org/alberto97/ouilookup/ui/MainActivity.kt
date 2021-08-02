package org.alberto97.ouilookup.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.work.ExperimentalExpeditedWork
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.alberto97.ouilookup.ui.theme.OUILookupTheme

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @ExperimentalCoroutinesApi
    @ExperimentalExpeditedWork
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