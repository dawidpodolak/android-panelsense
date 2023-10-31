package com.panelsense.app.ui.main.panel

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.panelsense.app.ui.main.EntityInteractor
import com.panelsense.core.model.icon.IconSpec
import com.panelsense.domain.model.Panel

@Suppress("EmptyFunctionBlock")
@Composable
fun HomePanelView(homePanel: Panel.HomePanel, entityInteractor: EntityInteractor) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .applyBackground(homePanel.background)
    ) {

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = homePanel.id ?: "Home Panel"
        )
        var drawable by remember { mutableStateOf<Drawable?>(null) }

        LaunchedEffect(key1 = "null") {
            drawable = entityInteractor.getIconProvider().getIcon(
                iconSpec = IconSpec(
                    name = "weather-partly-cloudy",
                    color = Color.Black.value.toInt()
                )
            )
        }
        Image(
            painter = rememberDrawablePainter(drawable = drawable),
            contentDescription = "te",
            modifier = Modifier.fillMaxSize(0.4f),
            alignment = Alignment.Center
        )
    }
}
