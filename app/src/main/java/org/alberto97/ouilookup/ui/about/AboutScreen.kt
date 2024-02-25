package org.alberto97.ouilookup.ui.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.*
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

@Composable
fun AboutScreen(
    viewModel: AboutViewModel,
    navController: NavController
) {
    val appStore = viewModel.availableAppStore
    val appVersion = viewModel.appVersion
    val lastDbUpdate: String by viewModel.dbVersion.collectAsState("")
    val useDynamicTheme by viewModel.useDynamicTheme.collectAsState()

    AboutScreen(
        appStore = appStore,
        appVersion = appVersion,
        lastDbUpdate = lastDbUpdate,
        openRepository = { viewModel.openRepository() },
        openAppStoreForReview = { viewModel.openAppStoreForReview() },
        openOtherApps =  { viewModel.openOtherApps() },
        supportedDynamicTheme = viewModel.supportedDynamicTheme,
        useDynamicTheme = useDynamicTheme,
        toggleDynamicTheme = { viewModel.toggleUseDynamicTheme() },
        onBackClick = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBackClick: () -> Unit,
    openRepository: () -> Unit,
    openAppStoreForReview: () -> Unit,
    openOtherApps: () -> Unit,
    appStore: AppStore,
    supportedDynamicTheme: Boolean,
    useDynamicTheme: Boolean,
    toggleDynamicTheme: () -> Unit,
    appVersion: String,
    lastDbUpdate: String
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.settings_title)) },
            navigationIcon = {
                IconButton(onClick = { onBackClick() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            },
        )
    }) { contentPadding ->
        Column(Modifier.padding(contentPadding)) {
            ListItem(
                leadingContent = { ListIcon { Icon(painterResource(R.drawable.ic_github), null) } },
                headlineContent = { Text(stringResource(R.string.about_repository_title)) },
                modifier = Modifier.clickable { openRepository() }
            )
            ListItem(
                leadingContent = { ListIcon { Icon(Icons.Rounded.Favorite, null) } },
                headlineContent = { Text(stringResource(R.string.about_leave_feedback_title)) },
                modifier = Modifier.clickable { openAppStoreForReview() }
            )
            ListItem(
                leadingContent = { OtherAppsIcon(appStore) },
                headlineContent = { Text(stringResource(R.string.about_other_apps_title)) },
                modifier = Modifier.clickable { openOtherApps() }
            )

            if (supportedDynamicTheme)
                Settings(useDynamicTheme, toggleDynamicTheme)

            HorizontalDivider(color = Color.LightGray)
            ListItem(
                leadingContent = {},
                headlineContent = {
                    Text(
                        stringResource(R.string.about_category_info),
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            )
            ListItem(
                leadingContent = {},
                headlineContent = { Text(stringResource(R.string.about_version)) },
                supportingContent = { Text(appVersion) }
            )
            ListItem(
                leadingContent = {},
                headlineContent = { Text(stringResource(R.string.about_author)) },
                supportingContent = { Text(stringResource(R.string.about_author_fulfilling_ego)) }
            )
            ListItem(
                leadingContent = { },
                headlineContent = { Text(stringResource(R.string.about_last_db_update)) },
                supportingContent = { Text(lastDbUpdate) },
            )
            HorizontalDivider(color = Color.LightGray)
            ListItem(
                leadingContent = { UpdateInfoIcon() },
                headlineContent = {},
                supportingContent = { Text(stringResource(R.string.about_db_updates_description)) }
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
private fun Settings(useDynamicTheme: Boolean, toggleDynamicTheme: () -> Unit) {
    Column {
        HorizontalDivider(color = Color.LightGray)
        ListItem(
            leadingContent = {},
            headlineContent = {
                Text(
                    stringResource(R.string.settings_title),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
        )
        ListItem(
            modifier = Modifier.clickable { toggleDynamicTheme() },
            leadingContent = {},
            headlineContent = { Text(stringResource(R.string.settings_dynamic_theme_title)) },
            supportingContent = { Text(stringResource(R.string.settings_dynamic_theme_text)) },
            trailingContent = {
                Switch(
                    checked = useDynamicTheme,
                    onCheckedChange = { toggleDynamicTheme() })
            }
        )
    }
}

@Composable
private fun UpdateInfoIcon() {
    CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.copy(alpha = 0.4f)) {
        ListIcon {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    OUILookupTheme {
        Surface {
            AboutScreen(
                onBackClick = { },
                openRepository = { },
                openAppStoreForReview = { },
                openOtherApps = { },
                appStore = AppStore.PlayStore,
                supportedDynamicTheme = true,
                useDynamicTheme = false,
                toggleDynamicTheme = {},
                appVersion = "1.0",
                lastDbUpdate = "Today"
            )
        }
    }
}
