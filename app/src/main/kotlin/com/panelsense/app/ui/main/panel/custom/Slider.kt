package com.panelsense.app.ui.main.panel.custom

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun VerticalSlider(
    modifier: Modifier = Modifier,
    initOn: Boolean = true,
    upSideDown: Boolean = false,
    maxValue: Float = 100f,
    initValue: Float = 0f,
    initColor: Color = Color(0xFF00BCD4),
    onUpdateValue: (Float) -> Unit = {}
) {
    var isDragged by remember { mutableStateOf(false) }
    var on by remember { mutableStateOf(initOn) }
    var color by remember { mutableStateOf(initColor) }
    var sliderValue by remember { mutableFloatStateOf(initValue.coerceIn(0f, maxValue)) }

    val animatedColor by animateColorAsState(targetValue = color, label = color.toString())
    val animatedSlideValue by animateFloatAsState(
        targetValue = sliderValue,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
        label = "initValue"
    )

    LaunchedEffect(key1 = initValue * initColor.hashCode() * initOn.hashCode()) {
        if (!isDragged) {
            sliderValue = initValue
            color = initColor
            on = initOn
        }
    }

    Box(modifier = modifier
        .run {
            if (upSideDown) rotate(180f) else this
        }
        .pointerInput(Unit) {
            detectVerticalDragGestures(
                onDragStart = { isDragged = true },
                onDragEnd = { isDragged = false }
            ) { change, _ ->
                if (!on) return@detectVerticalDragGestures
                val minHeight = size.width * 0.6f
                val position = change.position.y
                val newP =
                    maxValue - ((position.minus(minHeight.div(2)) * maxValue) / size.height.minus(
                        minHeight
                    ))
                sliderValue = newP.coerceIn(0f, maxValue)
                onUpdateValue(sliderValue)
            }
        }
        .aspectRatio(0.3f)) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            val cornerRadius = CornerRadius(size.width * 0.3f)
            val strokeWidth = cornerRadius.x * 0.1f
            val minHeight = cornerRadius.x * 2
            val maxHeight = size.height - strokeWidth - minHeight
            val requiredHeight = minHeight + (maxHeight * (animatedSlideValue / maxValue))
            val bottom = size.height - strokeWidth / 2
            val offsetY = strokeWidth / 2 + (bottom - requiredHeight)

            drawRoundRect(
                color = animatedColor.getBackground(),
                topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                size = size.copy(
                    width = size.width - strokeWidth,
                    height = size.height - strokeWidth / 2
                ),
                cornerRadius = cornerRadius,
            )
            if (on) {
                drawRoundRect(
                    color = animatedColor,
                    topLeft = Offset(strokeWidth / 2, offsetY),
                    size = size.copy(width = size.width - strokeWidth, height = requiredHeight),
                    cornerRadius = cornerRadius,
                )
                drawLine(
                    color = Color.White,
                    start = Offset(cornerRadius.x, offsetY + cornerRadius.y),
                    end = Offset(size.width - cornerRadius.x, offsetY + cornerRadius.y),
                    strokeWidth = strokeWidth * 1.5f,
                    cap = StrokeCap.Round,
                )
            }
        }
    }
}

fun Color.getBackground(darkFactor: Float = 0.5f): Color = copy(
    alpha = alpha * darkFactor,
    red = red * darkFactor,
    green = green * darkFactor,
    blue = blue * darkFactor,
)

@Preview(widthDp = 40, heightDp = 200)
@Composable()
fun Preview() {
    VerticalSlider(
        initValue = 0f,
        maxValue = 250f,
        initOn = true,
        upSideDown = false
    )
}
