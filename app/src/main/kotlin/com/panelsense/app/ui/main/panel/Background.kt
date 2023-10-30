package com.panelsense.app.ui.main.panel

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import com.panelsense.app.R

@Composable
fun Background(imageUrl: String?) {
    when (val bg = imageUrl.getBackgroundType()) {
        is BackgroundType.BackgroundURL -> {
            val painter = rememberAsyncImagePainter(model = bg.url)
            Image(
                painter = painter,
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        is BackgroundType.BackgroundColor -> Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bg.color)
        )

        is BackgroundType.BackgroundNone -> {
            val painter = painterResource(id = R.drawable.background)
            Image(
                painter = painter,
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
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
        HEX_REGEX.matches(this) -> BackgroundType.BackgroundColor(
            Color(
                android.graphics.Color.parseColor(
                    this
                )
            )
        )

        Regex(Patterns.WEB_URL.pattern()).matches(this) -> BackgroundType.BackgroundURL(this)
        else -> BackgroundType.BackgroundNone
    }
}

private val HEX_REGEX = "^#(?:[0-9a-fA-F]{3,4}){1,2}\$".toRegex()
