package com.panelsense.app.ui.main.panel

import android.graphics.Color.parseColor
import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.panelsense.app.ui.main.panel.item.PanelItemLayoutRequest
import com.panelsense.app.ui.main.panel.item.PanelItemViewType
import com.panelsense.app.ui.main.panel.item.getItemViewType
import com.panelsense.app.ui.theme.PanelItemBackgroundColor
import com.panelsense.domain.model.PanelItem

fun Modifier.applyBackground(background: String?): Modifier =
    composed {
        when (val backgroundType = background.getBackgroundType()) {
            is BackgroundType.BackgroundColor -> this.background(backgroundType.color)
            is BackgroundType.BackgroundURL -> this.paint(
                rememberAsyncImagePainter(model = backgroundType.url),
                contentScale = ContentScale.Crop
            )

            else -> this
        }
    }

fun Modifier.applyBackgroundForItem(
    panelItem: PanelItem,
    layoutRequest: PanelItemLayoutRequest
): Modifier {
    return when {
        layoutRequest is PanelItemLayoutRequest.Grid -> this
        panelItem.getItemViewType() in listOf(
            PanelItemViewType.WEATHER,
            PanelItemViewType.CLOCK
        ) -> this

        else -> this.background(
            color = PanelItemBackgroundColor,
            shape = ButtonShape
        )
    }
}

sealed class BackgroundType {

    data class BackgroundURL(val url: String) : BackgroundType()
    data class BackgroundColor(val color: Color) : BackgroundType()
    object BackgroundNone : BackgroundType()
}

private fun String?.getBackgroundType(): BackgroundType {

    return when {
        this == null -> BackgroundType.BackgroundNone
        HEX_REGEX.matches(this) -> kotlin.runCatching {
            BackgroundType.BackgroundColor(Color(parseColor(this)))
        }.getOrElse { BackgroundType.BackgroundNone }

        Regex(Patterns.WEB_URL.pattern()).matches(this) -> BackgroundType.BackgroundURL(this)
        else -> BackgroundType.BackgroundNone
    }
}

private val HEX_REGEX = "^#(?:[0-9a-fA-F]{3,4}){1,2}\$".toRegex()
