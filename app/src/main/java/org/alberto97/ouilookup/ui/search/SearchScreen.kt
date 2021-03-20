package org.alberto97.ouilookup.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.alberto97.ouilookup.ui.theme.OUILookupTheme
import org.alberto97.ouilookup.ui.theme.Purple500

@ExperimentalMaterialApi
@Composable
fun SearchScreen() {
    val viewModel: SearchViewModel = viewModel()

    val text: String by viewModel.text.observeAsState("")
    val option: String by viewModel.option.observeAsState("Address")
    val list: List<Pair<String, String>> by viewModel.list.observeAsState(listOf())

    SearchScreen(
        text = text,
        onTextChange = { value -> viewModel.onTextChange(value) },
        option = option,
        onOptionChange = { value -> viewModel.onTypeChange(value) },
        list = list
    )
}

@ExperimentalMaterialApi
@Composable
fun SearchScreen(
    text: String,
    onTextChange: (value: String) -> Unit,
    option: String,
    onOptionChange: (value: String) -> Unit,
    list: List<Pair<String, String>>
) {
    Scaffold(topBar = {
        TopAppBar({ Text("MAC Address Lookup") }, elevation = 0.dp)
    }) {
        Column {
            SearchOptions(
                text = text,
                onTextChange = onTextChange,
                option = option,
                onOptionChange = onOptionChange
            )
            Items(list = list)
        }
    }

}

@Composable
fun SearchOptions(
    text: String,
    onTextChange: (value: String) -> Unit,
    option: String,
    onOptionChange: (value: String) -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.primarySurface)
            .fillMaxWidth()
    ) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            trailingIcon = { Icon(Icons.Outlined.Search, null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        RadioGroup(
            option = option,
            onChange = onOptionChange,
            options = listOf("Address", "Organization")
        )
    }
}

@Composable
fun RadioGroup(option: String, onChange: (option: String) -> Unit, options: List<String>) {
    Row(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()) {
        options.forEach {
            Box(Modifier.weight(1f)) {
                Radio(
                    selected = option == it,
                    onClick = { onChange(it) },
                    it
                )
            }
        }
    }
}

@Composable
fun Radio(
    selected: Boolean,
    onClick: (() -> Unit)?,
    label: String
) {
    Row {
        RadioButton(selected = selected, onClick = onClick)
        Text(label, modifier = Modifier.padding(horizontal = 4.dp))
    }
}

@ExperimentalMaterialApi
@Composable
fun Items(list: List<Pair<String, String>>) {
    LazyColumn {
        items(list) { device ->
            ListItem(
                text = { Text(device.first) },
                secondaryText = { Text(device.second) }
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview()
@Composable
fun DefaultPreview() {
    OUILookupTheme {
        Surface {
            SearchScreen(
                text = "Test",
                onTextChange = {  },
                option = "Address",
                onOptionChange = {  },
                list = listOf(Pair("Apple", "AA:AA:AA"), Pair("Google", "FF:FF:FF"))
            )
        }
    }
}