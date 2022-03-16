package org.alberto97.ouilookup.ui.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Android
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.alberto97.ouilookup.R
import org.alberto97.ouilookup.ui.theme.OUILookupTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AboutScreen(
    viewModel: AboutViewModel,
    navController: NavController
) {
    val appStore = viewModel.availableAppStore
    val appVersion = viewModel.appVersion
    val lastDbUpdate: String by viewModel.dbVersion.collectAsState("")

    AboutScreen(
        appStore = appStore,
        appVersion = appVersion,
        lastDbUpdate = lastDbUpdate,
        openRepository = { viewModel.openRepository() },
        openOtherApps =  { viewModel.openOtherApps() },
        onBackClick = { navController.popBackStack() }
    )
}

@ExperimentalMaterialApi
@Composable
fun AboutScreen(
    onBackClick: () -> Unit,
    openRepository: () -> Unit,
    openOtherApps: () -> Unit,
    appStore: AppStore,
    appVersion: String,
    lastDbUpdate: String
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.about_title)) },
            navigationIcon = {
                IconButton(onClick = { onBackClick() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            },
        )
    }) {
        Column {
            ListItem(
                icon = { ListIcon { Icon(painterResource(R.drawable.ic_github), null) } },
                text = { Text(stringResource(R.string.about_repository_title)) },
                modifier = Modifier.clickable { openRepository() }
            )
            ListItem(
                icon = { OtherAppsIcon(appStore) },
                text = { Text(stringResource(R.string.about_other_apps_title)) },
                modifier = Modifier.clickable { openOtherApps() }
            )
            Divider(color = Color.LightGray)
            ListItem(
                icon = {},
                text = {
                    Text(
                        stringResource(R.string.about_category_info),
                        style = MaterialTheme.typography.subtitle2.copy(
                            color = MaterialTheme.colors.primary
                        )
                    )
                }
            )
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
            Divider(color = Color.LightGray)
            ListItem(
                icon = { UpdateInfoIcon() },
                text = {},
                secondaryText = { Text(stringResource(R.string.about_db_updates_description)) }
            )
        }
    }
}

@Composable
private fun OtherAppsIcon(store: AppStore) {
    ListIcon {
        if (store == AppStore.PlayStore)
            Icon(painterResource(R.drawable.ic_google_play), null)
        else
            Icon(Icons.Rounded.Android, null)
    }
}

@Composable
private fun ListIcon(child: @Composable () -> Unit) {
    Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
        child()
    }
}

@Composable
private fun UpdateInfoIcon() {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
        ListIcon {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun Preview() {
    OUILookupTheme {
        Surface {
            AboutScreen({ }, { }, { }, AppStore.PlayStore, "1.0", "Today")
        }
    }
}