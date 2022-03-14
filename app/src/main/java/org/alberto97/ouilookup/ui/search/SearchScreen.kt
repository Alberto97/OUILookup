package org.alberto97.ouilookup.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.alberto97.ouilookup.R
import org.alberto97.ouilookup.db.Oui
import org.alberto97.ouilookup.ui.Destinations
import org.alberto97.ouilookup.ui.common.OnResumeEffect
import org.alberto97.ouilookup.ui.theme.OUILookupTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
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

@ExperimentalMaterialApi
@Composable
fun SearchScreen(
    onInfoClick: () -> Unit,
    text: String,
    onTextChange: (value: String) -> Unit,
    list: List<Oui>,
    placeholder: UiSearchPlaceholder?
) {
    val toolbarHeight = 48.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

    val searchbarHeight = 80.dp
    val searchbarTopPaddingPx = remember { mutableStateOf(toolbarHeightPx) }

    // Create connection to the nested scroll system and listen to the scroll
    // happening inside child LazyColumn
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // try to consume before LazyColumn to collapse toolbar if needed, hence pre-scroll
                val delta = available.y

                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)

                val newPadding = searchbarTopPaddingPx.value + delta
                searchbarTopPaddingPx.value = newPadding.coerceIn(0f, toolbarHeightPx)

                // here's the catch: let's pretend we consumed 0 in any case, since we want
                // LazyColumn to scroll anyway for good UX
                // We're basically watching scroll without taking it
                return Offset.Zero
            }
        }
    }

    Scaffold {
        Box(
            Modifier
                .fillMaxSize()
                // attach as a parent to the nested scroll system
                .nestedScroll(nestedScrollConnection)
        ) {
            Content(
                list = list,
                placeholder = placeholder,
                listContentTopPadding = toolbarHeight + searchbarHeight,
            )
            Searchbar(
                text = text,
                onTextChange = onTextChange,
                onTrailingIconClick = { onTextChange("") },
                modifier = Modifier.padding(
                    top = with(LocalDensity.current) { searchbarTopPaddingPx.value.toDp() }
                )
            )
            SearchToolbar(
                dropdownMenuItems = {
                    DropdownMenuItem(onClick = { onInfoClick() }) {
                        Text(stringResource(R.string.search_action_about))
                    }
                },
                modifier = Modifier
                    .height(toolbarHeight)
                    .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun Content(
    list: List<Oui>,
    placeholder: UiSearchPlaceholder?,
    listContentTopPadding: Dp
) {
    if (placeholder != null)
        SearchPlaceholder(placeholder = placeholder)
    else
        LazyColumn(
            contentPadding = PaddingValues(top = listContentTopPadding),
        ) {
            items(list) { device ->
                ListItem(
                    text = { Text(device.orgName) },
                    secondaryText = { Text(device.oui) }
                )
            }
        }
}

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
