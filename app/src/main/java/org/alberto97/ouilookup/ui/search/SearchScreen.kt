package org.alberto97.ouilookup.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.CorporateFare
import androidx.compose.material.icons.outlined.DeveloperBoard
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import org.alberto97.ouilookup.db.Oui
import org.alberto97.ouilookup.ui.Destinations
import org.alberto97.ouilookup.ui.theme.OUILookupTheme

@ExperimentalMaterialApi
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navController: NavController
) {
    val text: String by viewModel.text.observeAsState("")
    val option: Int by viewModel.filter.observeAsState(0)
    val list: List<Oui> by viewModel.list.observeAsState(listOf())

    SearchScreen(
        onInfoClick = { navController.navigate(Destinations.ABOUT_ROUTE) },
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
    text: String,
    onTextChange: (value: String) -> Unit,
    tab: Int,
    onTabChange: (value: Int) -> Unit,
    list: List<Oui>
) {
    val (dropdownExpanded, setDropdownExpanded) = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MAC Address Lookup") },
                elevation = 0.dp,
                actions = {
                    IconButton(onClick = { setDropdownExpanded(true) }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More")
                        Dropdown(
                            expanded = dropdownExpanded,
                            onDismissRequest = { setDropdownExpanded(false) },
                            onInfoClick = onInfoClick
                        )
                    }
                }
            )
        }
    ) {
        Column {
            SearchOptions(
                text = text,
                onTextChange = onTextChange,
                tab = tab,
                onTabChange = onTabChange
            )
            Items(list = list)
        }
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
            TextField(
                value = text,
                onValueChange = onTextChange,
                leadingIcon = { Icon(Icons.Outlined.Search, null) },
                shape = MaterialTheme.shapes.small,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Tabs(
                option = tab,
                onChange = onTabChange
            )
        }
    }
}

@Composable
fun Tabs(
    option: Int,
    onChange: (option: Int) -> Unit,
) {
    TabRow(selectedTabIndex = option) {
            Tab(
                icon = { Icon(Icons.Outlined.DeveloperBoard, null)},
                text = { Text("Address") },
                selected = option == 0,
                onClick = { onChange(0) },
            )
            Tab(
                icon = { Icon(Icons.Outlined.CorporateFare, null)},
                text = { Text("Organization") },
                selected = option == 1,
                onClick = { onChange(1) },
            )
    }
}

@ExperimentalMaterialApi
@Composable
fun Items(list: List<Oui>) {
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
                text = "Test",
                onTextChange = { },
                tab = 0,
                onTabChange = { },
                list = listOf(
                    Oui("AA:AA:AA", "Apple", ""),
                    Oui("FF:FF:FF", "Google", "")
                )
            )
        }
    }
}