package org.alberto97.ouilookup.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.alberto97.ouilookup.R
import org.alberto97.ouilookup.db.Oui
import org.alberto97.ouilookup.ui.Destinations
import org.alberto97.ouilookup.ui.FullscreenPlaceholder
import org.alberto97.ouilookup.ui.theme.OUILookupTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navController: NavController
) {
    val text: String by viewModel.text.collectAsState("")
    val list: List<Oui> by viewModel.list.collectAsState(listOf())
    val updatingDb: Boolean by viewModel.updatingDb.collectAsState(false)

    SearchScreen(
        onInfoClick = { navController.navigate(Destinations.ABOUT_ROUTE) },
        text = text,
        onTextChange = { value -> viewModel.onTextChange(value) },
        list = list,
        updatingDb = updatingDb
    )
}

@ExperimentalMaterialApi
@Composable
fun SearchScreen(
    onInfoClick: () -> Unit,
    text: String,
    onTextChange: (value: String) -> Unit,
    list: List<Oui>,
    updatingDb: Boolean
) {
    val toolbarHeight = 48.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

    val searchbarHeight = 72.dp
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
                updatingDb = updatingDb,
                listContentTopPadding = toolbarHeight + searchbarHeight,
            )
            SearchBar(
                text = text,
                onTextChange = onTextChange,
                searchbarTopPadding = with(LocalDensity.current) { searchbarTopPaddingPx.value.toDp() }
            )
            SearchToolbar(
                dropdownMenuItems = {
                    DropdownMenuItem(onClick = { onInfoClick() }) {
                        Text(stringResource(R.string.search_action_about))
                    }
                },
                elevation = 0.dp,
                modifier = Modifier
                    .height(toolbarHeight)
                    .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
            )
        }
    }
}

@Composable
fun SearchBar(
    text: String,
    onTextChange: (value: String) -> Unit,
    searchbarTopPadding: Dp,
) {
    Surface(
        color = MaterialTheme.colors.primarySurface,
        elevation = AppBarDefaults.TopAppBarElevation,
        modifier = Modifier.padding(top = searchbarTopPadding)
    ) {
        TextField(
            value = text,
            placeholder = { Text(stringResource(R.string.search_text_field_placeholder)) },
            onValueChange = onTextChange,
            leadingIcon = { Icon(Icons.Outlined.Search, null) },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                backgroundColor = MaterialTheme.colors.surface,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun Content(
    list: List<Oui>,
    updatingDb: Boolean,
    listContentTopPadding: Dp
) {
    if (updatingDb)
        FullscreenPlaceholder(
            text = stringResource(R.string.search_update_database),
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(62.dp)
                    .padding(8.dp)
            )
        }
    else
        Items(
            list = list,
            listContentTopPadding
        )
}

@ExperimentalMaterialApi
@Composable
fun Items(list: List<Oui>, listContentTopPadding: Dp) {
    if (list.isEmpty())
        FullscreenPlaceholder(
            text = stringResource(R.string.search_results_not_found),
            icon = Icons.Outlined.SearchOff
        )
    else
        LazyColumn(
            contentPadding = PaddingValues(top = listContentTopPadding),
            modifier = Modifier.padding(12.dp)
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
            updatingDb = false
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
            updatingDb = false
        )
    }
}

@ExperimentalMaterialApi
@Preview("Updating DB")
@Composable
fun UpdatingDbPreview() {
    OUILookupTheme {
        SearchScreen(
            onInfoClick = { },
            text = "",
            onTextChange = { },
            list = emptyList(),
            updatingDb = true
        )
    }
}