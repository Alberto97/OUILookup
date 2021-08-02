package org.alberto97.ouilookup.ui.about

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import org.alberto97.ouilookup.R
import org.alberto97.ouilookup.ui.theme.OUILookupTheme

@ExperimentalMaterialApi
@Composable
fun AboutScreen(
    viewModel: AboutViewModel,
    navController: NavController
) {
    val appVersion = viewModel.appVersion
    val lastDbUpdate: String by viewModel.dbVersion.collectAsState("")

    AboutScreen(
        appVersion = appVersion,
        lastDbUpdate = lastDbUpdate,
        onBackClick = { navController.popBackStack() }
    )
}

@ExperimentalMaterialApi
@Composable
fun AboutScreen(
    onBackClick: () -> Unit,
    appVersion: String,
    lastDbUpdate: String
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Info") },
            navigationIcon = {
                IconButton(onClick = { onBackClick() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            },
        )
    }) {
        Column {
            ListItem(
                icon = {},
                text = { Text(stringResource(R.string.about_version)) },
                secondaryText = { Text(appVersion) }
            )
            ListItem(
                icon = {},
                text = { Text(stringResource(R.string.about_author)) },
                secondaryText = { Text(stringResource(R.string.about_author_fulfilling_ego)) }
            )
            ListItem(
                icon = { },
                text = { Text(stringResource(R.string.about_last_db_update)) },
                secondaryText = { Text(lastDbUpdate) },
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun Preview() {
    OUILookupTheme {
        Surface {
            AboutScreen({ },"1.0", "Today")
        }
    }
}