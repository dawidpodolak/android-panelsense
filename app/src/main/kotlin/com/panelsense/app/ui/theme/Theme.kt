package com.panelsense.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val PanelSenseColorPalette = lightColorScheme(
    primary = Primary,
    inversePrimary = InversePrimary,
    secondary = Secondary
)

@Composable
fun PanelSenseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PanelSenseColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
