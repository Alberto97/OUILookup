package org.alberto97.ouilookup.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.alberto97.ouilookup.repository.ISettingsRepository
import org.alberto97.ouilookup.ui.theme.OUILookupTheme

@Composable
fun OuiLookupApp(settings: ISettingsRepository) {
    val useDynamicTheme by settings.getUseDynamicTheme().collectAsState(false)

    OUILookupTheme(dynamicColor = useDynamicTheme) {
        NavGraph()
    }
}
