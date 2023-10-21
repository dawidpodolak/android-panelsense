package com.panelsense.app.ui.main.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColorScheme(
    primary = Purple200,
    inversePrimary = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColorScheme(
    primary = Purple500,
    inversePrimary = Purple700,
    secondary = Teal200
)

@Composable
fun NsPanelSenseTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
