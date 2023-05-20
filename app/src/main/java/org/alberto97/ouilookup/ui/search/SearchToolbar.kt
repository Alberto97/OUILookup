package org.alberto97.ouilookup.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.alberto97.ouilookup.R
import org.alberto97.ouilookup.ui.theme.whiteRabbitFamily

@Composable
fun Title() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) {
            Icon(Icons.Default.DeveloperBoard, null)
            Box(modifier = Modifier.width(8.dp))
            Text(
                stringResource(R.string.search_toolbar_title),
                fontFamily = whiteRabbitFamily,
                style = MaterialTheme.typography.headlineSmall
            )
            Box(modifier = Modifier.width(24.dp))
        }
    }
}

@Composable
fun ActionsDropdown(dropdownMenuItems: @Composable ColumnScope.() -> Unit) {
    val (dropdownExpanded, setDropdownExpanded) = remember { mutableStateOf(false) }

    IconButton(onClick = { setDropdownExpanded(true) }) {
        Icon(Icons.Filled.MoreVert, contentDescription = "More")
        DropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { setDropdownExpanded(false) }
        ) {
            dropdownMenuItems()
        }
    }
}
