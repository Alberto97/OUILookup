package org.alberto97.ouilookup.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.work.ExperimentalExpeditedWork
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.alberto97.ouilookup.db.Oui
import org.alberto97.ouilookup.ui.Destinations
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

    SearchScreen(
        onInfoClick = { navController.navigate(Destinations.ABOUT_ROUTE) },
        text = text,
        onTextChange = { value -> viewModel.onTextChange(value) },
        list = list
    )
}

@ExperimentalMaterialApi
@Composable
fun SearchScreen(
    onInfoClick: () -> Unit,
    text: String,
    onTextChange: (value: String) -> Unit,
    list: List<Oui>
) {

    Scaffold {
        Column(
            modifier = Modifier.background(MaterialTheme.colors.primarySurface)
        ) {
            SearchOptions(
                onInfoClick = onInfoClick,
                text = text,
                onTextChange = onTextChange
            )
            Surface(
                shape = RoundedCornerShape(
                    topStart = 12.dp,
                    topEnd = 12.dp
                )
            ) {
                Items(
                    list = list
                )
            }
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
            Text("Info")
        }
    }
}

@Composable
fun SearchOptions(
    onInfoClick: () -> Unit,
    text: String,
    onTextChange: (value: String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        SearchBar(
            onInfoClick  = onInfoClick,
            text = text,
            onTextChange = onTextChange
        )
    }
}

@Composable
fun SearchBar(
    onInfoClick: () -> Unit,
    text: String,
    onTextChange: (value: String) -> Unit,
) {
    TextField(
        value = text,
        placeholder = { Text("Search MAC or organization") },
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
            .fillMaxWidth().padding(16.dp)
    )
}

@ExperimentalMaterialApi
@Composable
fun Items(list: List<Oui>) {
    if (list.isEmpty())
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Outlined.SearchOff,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(62.dp)
            )
            Text(
                text = "No results found",
                color = Color.LightGray,
                style = MaterialTheme.typography.subtitle1,
            )
        }
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
        )
    }
}