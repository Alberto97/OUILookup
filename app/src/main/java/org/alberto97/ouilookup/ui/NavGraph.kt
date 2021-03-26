package org.alberto97.ouilookup.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.alberto97.ouilookup.ui.about.AboutScreen
import org.alberto97.ouilookup.ui.about.AboutViewModel
import org.alberto97.ouilookup.ui.search.SearchScreen
import org.alberto97.ouilookup.ui.search.SearchViewModel

object Destinations {
    const val SEARCH_ROUTE = "search"
    const val ABOUT_ROUTE = "about"
}

@ExperimentalMaterialApi
@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Destinations.SEARCH_ROUTE) {
        composable(Destinations.SEARCH_ROUTE) {
            val viewModel: SearchViewModel = hiltNavGraphViewModel(it)
            SearchScreen(viewModel, navController)
        }
        composable(Destinations.ABOUT_ROUTE) {
            val viewModel: AboutViewModel = hiltNavGraphViewModel(it)
            AboutScreen(viewModel, navController)
        }
    }
}