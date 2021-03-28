package org.alberto97.ouilookup.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SettingsEthernet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import org.alberto97.ouilookup.ui.Destinations
import org.alberto97.ouilookup.ui.theme.OUILookupTheme
import org.alberto97.ouilookup.ui.theme.Purple500

@Composable
fun SplashScreen(splashViewModel: SplashViewModel, navController: NavController) {
    val isUpdating: Boolean by splashViewModel.isUpdating.observeAsState(false)
    val navigate: Boolean by splashViewModel.navigate.observeAsState(false)

    if (navigate)
        navController.navigate(Destinations.SEARCH_ROUTE) {
            popUpTo(0) { inclusive = true }
        }

    SplashScreen(isUpdating = isUpdating)
}

@Composable
fun SplashScreen(isUpdating: Boolean) {
    Scaffold {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Purple500)
        ) {
            Icon(
                Icons.Outlined.SettingsEthernet,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(128.dp)
            )

            if (isUpdating) {
                Text(
                    text = "Updating database...",
                    style = MaterialTheme.typography.subtitle1.copy(color = Color.White)
                )
                LinearProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    OUILookupTheme {
        Surface {
            SplashScreen(false)
        }
    }
}