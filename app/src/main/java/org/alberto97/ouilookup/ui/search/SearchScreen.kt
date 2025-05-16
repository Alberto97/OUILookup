package org.alberto97.ouilookup.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.alberto97.ouilookup.R
import org.alberto97.ouilookup.db.Oui
import org.alberto97.ouilookup.ui.Destinations
import org.alberto97.ouilookup.ui.theme.OUILookupTheme

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navController: NavController,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val text: String by viewModel.text.collectAsState("")
    val list: List<Oui> by viewModel.list.collectAsState(listOf())
    val lookupList: List<String> by viewModel.bulkLookupList.collectAsState(listOf())
    val placeholder by viewModel.placeholder.collectAsState(UiSearchPlaceholder.Instructions)

    val clipboardPasteScope = rememberCoroutineScope()
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME, lifecycleOwner) {
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
        lookupList = lookupList,
        placeholder = placeholder
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    onInfoClick: () -> Unit,
    text: String,
    onTextChange: (value: String) -> Unit,
    list: List<Oui>,
    lookupList: List<String>,
    placeholder: UiSearchPlaceholder?
) {
    val scrollState = rememberLazyListState()
    val bottomBarVisible by remember { derivedStateOf { scrollState.firstVisibleItemIndex == 0 } }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        bottomBar = {
            NavigationBar(
                visible = bottomBarVisible,
                onSettingsClick = onInfoClick,
                onSearchClick = {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                },
            )
        }
    ) { contentPadding ->
        CompositionLocalProvider(
            // Disable overscroll effect
            LocalOverscrollConfiguration provides null
        ) {
            LazyColumn(
                Modifier
                    .padding(contentPadding)
                    .fillMaxWidth(),
            state = scrollState,
            ) {
                item {
                    Row(Modifier.padding(top = 26.dp)) {
                        Title()
                    }
                }
                stickyHeader {
                    Surface {
                        SearchBar(
                            modifier = Modifier.focusRequester(focusRequester),
                            text = text,
                            onTextChange = onTextChange,
                            onClear = { onTextChange("") }
                        )
                    }
                }
                item {
                    Box(Modifier.padding(horizontal = 14.dp)) {
                        ChipGroup(list = lookupList)
                    }
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
                            headlineContent = { Text(device.orgName) },
                            supportingContent = { Text(device.oui) }
                        )
                    }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    onClear: () -> Unit
) {
    SearchBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(14.dp),
        placeholder = { Text(stringResource(R.string.search_text_field_placeholder)) },
        leadingIcon = { Icon(Icons.Rounded.Search, null) },
        trailingIcon = { if (text.isNotEmpty()) ClearIcon(onClear) },
        query = text,
        onQueryChange = onTextChange,
        onSearch = {},
        active = false,
        onActiveChange = {},
        content = {},
    )
}

@Composable
private fun ClearIcon(onClick: () -> Unit) {
    IconButton(onClick) {
        Icon(Icons.Rounded.Clear, contentDescription = "Clear")
    }
}

@Composable
private fun ChipGroup(list: List<String>) {
    LazyRow {
        items(list) { item ->
            ElevatedSuggestionChip(
                onClick = {},
                modifier = Modifier.padding(horizontal = 4.dp),
                label = { Text(item) }
            )
        }
    }
}

@Composable
private fun NavigationBar(
    @Suppress("UNUSED_PARAMETER")
    visible: Boolean,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = true,
        enter = slideInVertically {
            with(density) { 80.dp.roundToPx() }
        },
        exit = slideOutVertically {
            with(density) { 80.dp.roundToPx() }
        }
    ) {
        NavigationBar {
            NavigationBarItem(
                onClick = onSearchClick,
                selected = true,
                icon = { Icon(Icons.Outlined.Search, "") },
                label = { Text(stringResource(R.string.action_search)) },
            )
            NavigationBarItem(
                onClick = onSettingsClick,
                selected = false,
                icon = { Icon(Icons.Outlined.Settings, "") },
                label = { Text(stringResource(R.string.settings_title)) }
            )
        }
    }
}

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
            lookupList = listOf("BB:BB:BB"),
            placeholder = null
        )
    }
}

@Preview("Empty list")
@Composable
fun EmptyPreview() {
    OUILookupTheme {
        SearchScreen(
            onInfoClick = { },
            text = "",
            onTextChange = { },
            list = emptyList(),
            lookupList = emptyList(),
            placeholder = UiSearchPlaceholder.NoResults
        )
    }
}
