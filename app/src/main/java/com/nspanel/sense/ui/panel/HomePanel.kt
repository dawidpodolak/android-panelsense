package com.nspanel.sense.ui.panel

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.nspanel.core.model.IconSpec
import com.nspanel.core.model.PanelConfiguration.HomePanel
import com.nspanel.data.icons.IconProvider

@Suppress("EmptyFunctionBlock")
@Composable
fun HomePanel(panel: HomePanel, iconProvider: IconProvider) {

    Box (
        modifier = Modifier.fillMaxSize(),
    ){

        var drawable = remember { mutableStateOf<Drawable?>(null) }

        LaunchedEffect(key1 = "null") {
            drawable.value = iconProvider.getIcon(iconSpec = IconSpec(
                name = "weather-partly-cloudy",
                color = Color.Black.value.toInt()
            ))
        }
        Image(
            painter = rememberDrawablePainter(drawable = drawable.value),
            contentDescription = "te",
            modifier = Modifier.fillMaxSize(0.4f),
            alignment = Alignment.Center)
    }
}
