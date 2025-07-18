package org.alberto97.ouilookup.ui.theme

import android.app.Activity
import android.os.Build
import android.view.Window
import androidx.annotation.ColorInt
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val darkColorScheme = darkColorScheme(
    primary = Blue200,
    secondary = Blue700,
    tertiary = Blue700,
    secondaryContainer = Color(0xFF3b4858),
    onSecondaryContainer = Color(0xFFd0e4ff)
)

private val lightColorScheme = lightColorScheme(
    primary = Blue500,
    secondary = Blue500,
    tertiary = Blue200,
    secondaryContainer = Color(0xFFd1e4ff),
    onSecondaryContainer = Color(0xFF001d34)
)

@Composable
fun OUILookupTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.colorStatusBarPreEdgeToEdge(colorScheme.surface.toArgb())
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

// Color the status bar only on Android 14 and lower where edge-to-edge is not enabled
fun Window.colorStatusBarPreEdgeToEdge(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
        @Suppress("DEPRECATION")
        statusBarColor = color
    }
}
