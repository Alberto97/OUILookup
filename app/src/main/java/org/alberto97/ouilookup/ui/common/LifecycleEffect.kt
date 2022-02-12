package org.alberto97.ouilookup.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

@Composable
fun LifecycleEffect(lifecycleOwner: LifecycleOwner, onEvent: (event: Lifecycle.Event) -> Unit) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            onEvent(event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun OnResumeEffect(lifecycleOwner: LifecycleOwner, onResume: () -> Unit) {
    LifecycleEffect(lifecycleOwner) { event ->
        if (event == Lifecycle.Event.ON_RESUME)
            onResume()
    }
}
