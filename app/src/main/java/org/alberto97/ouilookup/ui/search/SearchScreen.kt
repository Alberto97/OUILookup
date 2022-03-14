package org.alberto97.ouilookup.ui.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.alberto97.ouilookup.R
import org.alberto97.ouilookup.db.Oui
import org.alberto97.ouilookup.ui.Destinations
import org.alberto97.ouilookup.ui.common.OnResumeEffect
import org.alberto97.ouilookup.ui.theme.OUILookupTheme

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navController: NavController,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val text: String by viewModel.text.collectAsState("")
    val list: List<Oui> by viewModel.list.collectAsState(listOf())
    val placeholder by viewModel.placeholder.collectAsState(UiSearchPlaceholder.Instructions)

    val clipboardPasteScope = rememberCoroutineScope()
    OnResumeEffect(lifecycleOwner) {
        clipboardPasteScope.launch {
            // Wait for the app to get focus
            delay(500L)
            viewModel.checkClipboard()
        }
    }

    SearchScreen(
        onInfoClick = { navController.navigate(Destinations.ABOUT_ROUTE) },
        text = text,
        onTextChange = { value -> viewModel.onTextChange(value) },
        list = list,
        placeholder = placeholder
    )
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun SearchScreen(
    onInfoClick: () -> Unit,
    text: String,
    onTextChange: (value: String) -> Unit,
    list: List<Oui>,
    placeholder: UiSearchPlaceholder?
) {
    Scaffold {
        CompositionLocalProvider(
            // Disable overscroll effect
            LocalOverScrollConfiguration provides null
        ) {
            LazyColumn(Modifier.fillMaxWidth()) {
                item {
                    SearchToolbar(
                        dropdownMenuItems = {
                            DropdownMenuItem(onClick = { onInfoClick() }) {
                                Text(stringResource(R.string.search_action_about))
                            }
                        }
                    )
                }
                stickyHeader {
                    SearchbarWithShadow(
                        text = text,
                        onTextChange = onTextChange,
                        onTrailingIconClick = { onTextChange("") }
                    )
                }
                if (placeholder != null)
                    item {
                        Column(modifier = Modifier.fillParentMaxHeight(0.8f)) {
                            SearchPlaceholder(placeholder = placeholder)
                        }
                    }
                else
                    items(list) { device ->
                        ListItem(
                            text = { Text(device.orgName) },
                            secondaryText = { Text(device.oui) }
                        )
                    }
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Preview
@Composable
fun DefaultPreview() {
    OUILookupTheme {
        SearchScreen(
            onInfoClick = { },
            text = "Test",
            onTextChange = { },
            list = listOf(
                Oui("AA:AA:AA", "Apple", ""),
                Oui("FF:FF:FF", "Google", "")
            ),
            placeholder = null
        )
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Preview("Empty list")
@Composable
fun EmptyPreview() {
    OUILookupTheme {
        SearchScreen(
            onInfoClick = { },
            text = "",
            onTextChange = { },
            list = emptyList(),
            placeholder = UiSearchPlaceholder.NoResults
        )
    }
}
