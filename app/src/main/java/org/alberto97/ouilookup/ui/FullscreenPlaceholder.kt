package org.alberto97.ouilookup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun FullscreenPlaceholder(text: String, icon: ImageVector) {
    FullscreenPlaceholder(text = text) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(62.dp)
        )
    }
}

@Composable
fun FullscreenPlaceholder(text: String, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.copy(alpha = 0.4f)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.padding(8.dp)) {
                content()
            }
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}
