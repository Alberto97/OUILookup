package org.alberto97.ouilookup.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.work.ExperimentalExpeditedWork
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.alberto97.ouilookup.R
import org.alberto97.ouilookup.db.Oui
import org.alberto97.ouilookup.ui.Destinations
import org.alberto97.ouilookup.ui.FullscreenPlaceholder
import org.alberto97.ouilookup.ui.theme.OUILookupTheme

@ExperimentalCoroutinesApi
@ExperimentalExpeditedWork
@ExperimentalMaterialApi
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

    Scaffold {
        Column(
            modifier = Modifier.background(MaterialTheme.colors.primarySurface)
        ) {
            SearchBar(
                onInfoClick = onInfoClick,
                text = text,
                onTextChange = onTextChange
            )
            Content(
                list = list,
                updatingDb = updatingDb
            )
        }
    }
}

@Composable
fun DropdownButton(onInfoClick: () -> Unit) {
    val (dropdownExpanded, setDropdownExpanded) = remember { mutableStateOf(false) }

    IconButton(onClick = { setDropdownExpanded(true) }) {
        Icon(Icons.Filled.MoreVert, contentDescription = "More")
        Dropdown(
            expanded = dropdownExpanded,
            onDismissRequest = { setDropdownExpanded(false) },
            onInfoClick = onInfoClick
        )
    }
}

@Composable
fun Dropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onInfoClick: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest() }
    ) {
        DropdownMenuItem(onClick = { onInfoClick() }) {
            Text(stringResource(R.string.search_action_about))
        }
    }
}

@Composable
fun SearchBar(
    onInfoClick: () -> Unit,
    text: String,
    onTextChange: (value: String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = text,
            placeholder = { Text(stringResource(R.string.search_text_field_placeholder)) },
            onValueChange = onTextChange,
            leadingIcon = { Icon(
                Icons.Outlined.Search,
                contentDescription = null
            ) },
            trailingIcon = { DropdownButton(onInfoClick) },
            shape = RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 8.dp
            ),
            colors = TextFieldDefaults.textFieldColors(
                trailingIconColor = Color.White,
                textColor = Color.White,
                focusedIndicatorColor = Color.White,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun Content(
    list: List<Oui>,
    updatingDb: Boolean
) {
    Surface(
        shape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        )
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
                list = list
            )
    }
}

@ExperimentalMaterialApi
@Composable
fun Items(list: List<Oui>) {
    if (list.isEmpty())
        FullscreenPlaceholder(
            text = stringResource(R.string.search_results_not_found),
            icon = Icons.Outlined.SearchOff
        )
    else
        LazyColumn(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
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