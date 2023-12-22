package com.panelsense.app.ui.main.panel

import android.graphics.Color.parseColor
import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter

@Composable
fun Modifier.applyBackground(background: String?): Modifier =
    when (val backgroundType = background.getBackgroundType()) {
        is BackgroundType.BackgroundColor -> this.background(backgroundType.color)
        is BackgroundType.BackgroundURL -> this.paint(
            rememberAsyncImagePainter(model = backgroundType.url), contentScale = ContentScale.Crop
        )

        else -> this
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
