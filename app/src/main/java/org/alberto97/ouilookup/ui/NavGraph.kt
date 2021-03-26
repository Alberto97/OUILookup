package org.alberto97.ouilookup.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.alberto97.ouilookup.ui.search.SearchScreen
import org.alberto97.ouilookup.ui.search.SearchViewModel

object Destinations {
    const val SEARCH_ROUTE = "search"
}

@ExperimentalMaterialApi
@Composable
fun NavGraph() {

    val navController = rememberNavController()
    NavHost(navController, startDestination = Destinations.SEARCH_ROUTE) {
        composable(Destinations.SEARCH_ROUTE) {
            val viewModel: SearchViewModel = hiltNavGraphViewModel(it)
            SearchScreen(viewModel)
        }
    }
}