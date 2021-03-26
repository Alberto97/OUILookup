package org.alberto97.ouilookup.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CorporateFare
import androidx.compose.material.icons.outlined.DeveloperBoard
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.alberto97.ouilookup.db.Oui
import org.alberto97.ouilookup.ui.theme.OUILookupTheme

@ExperimentalMaterialApi
@Composable
fun SearchScreen() {
    val viewModel: SearchViewModel = viewModel()

    val text: String by viewModel.text.observeAsState("")
    val option: Int by viewModel.filter.observeAsState(0)
    val list: List<Oui> by viewModel.list.observeAsState(listOf())

    SearchScreen(
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
    text: String,
    onTextChange: (value: String) -> Unit,
    tab: Int,
    onTabChange: (value: Int) -> Unit,
    list: List<Oui>
) {
    Scaffold(topBar = {
        TopAppBar({ Text("MAC Address Lookup") }, elevation = 0.dp)
    }) {
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
fun SearchOptions(
    text: String,
    onTextChange: (value: String) -> Unit,
    tab: Int,
    onTabChange: (value: Int) -> Unit
) {
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
                text = "Test",
                onTextChange = {  },
                tab = 0,
                onTabChange = {  },
                list = listOf(
                    Oui("AA:AA:AA", "Apple", ""),
                    Oui("FF:FF:FF", "Google", "")
                )
            )
        }
    }
}