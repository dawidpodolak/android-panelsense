package com.panelsense.app.ui.main.panel

import android.graphics.Color.parseColor
import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.panelsense.app.ui.main.panel.item.PanelItemLayoutRequest
import com.panelsense.app.ui.main.panel.item.PanelItemViewType
import com.panelsense.app.ui.main.panel.item.getItemViewType
import com.panelsense.app.ui.theme.PanelItemBackgroundColor
import com.panelsense.domain.model.PanelItem
import kotlin.math.max

fun Modifier.applyBackground(background: String?, foreground: String? = null): Modifier =
    composed {
        when (val backgroundType = background.getBackgroundType()) {
            is BackgroundType.BackgroundColor -> this.background(backgroundType.color)
            is BackgroundType.BackgroundURL -> this
                .paint(
                    rememberAsyncImagePainter(model = backgroundType.url),
                    contentScale = ContentScale.Crop
                )
                .applyForeground(foreground)

            else -> this
        }
    }

fun Modifier.applyFinalBackgroundForItem(
    background: String?,
    foreground: String? = null
): Modifier =
    composed {
        when (val backgroundType = background.getBackgroundType()) {
            is BackgroundType.BackgroundColor -> this.background(backgroundType.color, ButtonShape)
            is BackgroundType.BackgroundURL -> {
                var intSize by remember { mutableStateOf(IntSize.Zero) }
                var sizeToIntrinsics by remember { mutableStateOf(false) }
                var painter = rememberAsyncImagePainter(model = backgroundType.url)
                this
                    .onSizeChanged {
                        if (painter.state !is AsyncImagePainter.State.Success) {
                            intSize = it
                        }
                    }
                    .paint(
                        painter = painter,
                        contentScale = BgCrop(intSize.height) { sizeToIntrinsics = it },
                        sizeToIntrinsics = sizeToIntrinsics
                    )
                    .applyForeground(foreground)
            }

            else -> this
        }
    }

fun Modifier.applyBackgroundForItem(
    panelItem: PanelItem,
    layoutRequest: PanelItemLayoutRequest
): Modifier {
    return when {
        layoutRequest is PanelItemLayoutRequest.Grid -> this
        panelItem.background != null -> this
            .clip(ButtonShape)
            .applyFinalBackgroundForItem(
                panelItem.background, panelItem.foreground
            )

        panelItem.getItemViewType() in listOf(
            PanelItemViewType.WEATHER,
            PanelItemViewType.CLOCK
        ) -> this

        else -> this.background(
            color = PanelItemBackgroundColor,
            shape = ButtonShape
        )
    }.run { this.clip(ButtonShape) }
}

fun Modifier.applyForeground(foreground: String?): Modifier = this.drawBehind {
    foreground?.let {
        val foregroundType = it.getBackgroundType()
        if (foregroundType is BackgroundType.BackgroundColor) {
            this.drawRect(
                foregroundType.color,
                Offset.Zero,
                this.size,
                1f,
                Fill,
                null
            )
        }
    }
}

fun BgCrop(maxHeight: Int, sizeToIntrinsics: (Boolean) -> Unit) = object : ContentScale {
    override fun computeScaleFactor(srcSize: Size, dstSize: Size): ScaleFactor =
        if (srcSize.height > srcSize.width) {
            val widthScale = dstSize.width / srcSize.width
            val heightScale = dstSize.height / srcSize.height
            sizeToIntrinsics(false)
            max(widthScale, heightScale)
        } else {
            sizeToIntrinsics(true)
            maxHeight.toFloat() / srcSize.height
        }.run {
            ScaleFactor(this, this)
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
