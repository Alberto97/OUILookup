package org.alberto97.ouilookup.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import org.alberto97.ouilookup.ui.about.AboutScreen
import org.alberto97.ouilookup.ui.about.AboutViewModel
import org.alberto97.ouilookup.ui.feedback.FeedbackScreen
import org.alberto97.ouilookup.ui.feedback.FeedbackViewModel
import org.alberto97.ouilookup.ui.search.SearchScreen
import org.alberto97.ouilookup.ui.search.SearchViewModel

object Destinations {
    const val SEARCH_ROUTE = "search"
    const val ABOUT_ROUTE = "about"
    const val FEEDBACK_ROUTE = "feedback"
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun NavGraph() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(navController, startDestination = Destinations.SEARCH_ROUTE) {
            composable(Destinations.SEARCH_ROUTE) {
                val viewModel: SearchViewModel = hiltViewModel(it)
                SearchScreen(viewModel, navController)
            }
            composable(Destinations.ABOUT_ROUTE) {
                val viewModel: AboutViewModel = hiltViewModel(it)
                AboutScreen(viewModel, navController)
            }
            bottomSheet(Destinations.FEEDBACK_ROUTE) {
                val viewModel: FeedbackViewModel = hiltViewModel(it)
                FeedbackScreen(viewModel, navController)
            }
        }
    }
}