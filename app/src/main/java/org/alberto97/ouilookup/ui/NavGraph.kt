package org.alberto97.ouilookup.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.alberto97.ouilookup.ui.about.AboutScreen
import org.alberto97.ouilookup.ui.about.AboutViewModel
import org.alberto97.ouilookup.ui.search.SearchScreen
import org.alberto97.ouilookup.ui.search.SearchViewModel
import org.alberto97.ouilookup.ui.splash.SplashScreen
import org.alberto97.ouilookup.ui.splash.SplashViewModel

object Destinations {
    const val SPLASH_ROUTE = "splash"
    const val SEARCH_ROUTE = "search"
    const val ABOUT_ROUTE = "about"
}

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Destinations.SPLASH_ROUTE) {
        composable(Destinations.SPLASH_ROUTE) {
            val viewModel: SplashViewModel = hiltViewModel(it)
            SplashScreen(viewModel, navController)
        }
        composable(Destinations.SEARCH_ROUTE) {
            val viewModel: SearchViewModel = hiltViewModel(it)
            SearchScreen(viewModel, navController)
        }
        composable(Destinations.ABOUT_ROUTE) {
            val viewModel: AboutViewModel = hiltViewModel(it)
            AboutScreen(viewModel, navController)
        }
    }
}