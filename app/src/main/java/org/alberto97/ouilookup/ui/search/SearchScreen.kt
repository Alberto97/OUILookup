package org.alberto97.ouilookup.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FindInPage
import androidx.compose.material.icons.outlined.Search
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
    val searchPlaceholder: String by viewModel.searchPlaceholder.collectAsState("")
    val text: String by viewModel.text.collectAsState("")
    val option: Int by viewModel.filter.collectAsState(0)
    val list: List<Oui> by viewModel.list.collectAsState(listOf())

    SearchScreen(
        onInfoClick = { navController.navigate(Destinations.ABOUT_ROUTE) },
        searchPlaceholder = searchPlaceholder,
        text = text,
        onTextChange = { value -> viewModel.onTextChange(value) },
        tab = option,
        onTabChange = { value -> viewModel.onFilterChange(value) },
        list = list
    )
}

@ExperimentalMaterialApi
@Composable
fun SearchScreen(
    onInfoClick: () -> Unit,
    searchPlaceholder: String,
    text: String,
    onTextChange: (value: String) -> Unit,
    tab: Int,
    onTabChange: (value: Int) -> Unit,
    list: List<Oui>
) {

    Scaffold {
        Column {
            SearchOptions(
                onInfoClick = onInfoClick,
                placeholder = searchPlaceholder,
                text = text,
                onTextChange = onTextChange,
                tab = tab,
                onTabChange = onTabChange
            )
            Items(
                list = list
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
fun Dropdown(expanded: Boolean, onDismissRequest: () -> Unit, onInfoClick: () -> Unit) {
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
    placeholder: String,
    text: String,
    onTextChange: (value: String) -> Unit,
    tab: Int,
    onTabChange: (value: Int) -> Unit
) {
    Surface(elevation = AppBarDefaults.TopAppBarElevation) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.primarySurface)
                .fillMaxWidth()
        ) {
            SearchBar(
                onInfoClick  = onInfoClick,
                placeholder = placeholder,
                text = text,
                onTextChange = onTextChange
            )
            Tabs(
                option = tab,
                onChange = onTabChange
            )
        }
    }
}

@Composable
fun SearchBar(
    onInfoClick: () -> Unit,
    placeholder: String,
    text: String,
    onTextChange: (value: String) -> Unit,
) {
    TextField(
        value = text,
        placeholder = { Text(placeholder)},
        onValueChange = onTextChange,
        leadingIcon = { Icon(Icons.Outlined.Search, null) },
        trailingIcon = { DropdownButton(onInfoClick) },
        shape = CircleShape,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun Tabs(
    option: Int,
    onChange: (option: Int) -> Unit,
) {
    TabRow(selectedTabIndex = option) {
            Tab(
                text = { Text("Address") },
                selected = option == 0,
                onClick = { onChange(0) },
            )
            Tab(
                text = { Text("Organization") },
                selected = option == 1,
                onClick = { onChange(1) },
            )
    }
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
                imageVector = Icons.Outlined.FindInPage,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(62.dp)
            )
            Text(
                text = "No results found",
                color = Color.LightGray,
                style = MaterialTheme.typography.subtitle1
            )
        }
    else
        LazyColumn {
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
        Surface {
            SearchScreen(
                onInfoClick = { },
                searchPlaceholder = "",
                text = "Test",
                onTextChange = { },
                tab = 0,
                onTabChange = { },
                list = listOf(
                    Oui("AA:AA:AA", "Apple", ""),
                    Oui("FF:FF:FF", "Google", "")
                ),
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview("Empty list")
@Composable
fun EmptyPreview() {
    OUILookupTheme {
        Surface {
            SearchScreen(
                onInfoClick = { },
                searchPlaceholder = "",
                text = "",
                onTextChange = { },
                tab = 0,
                onTabChange = { },
                list = emptyList(),
            )
        }
    }
}