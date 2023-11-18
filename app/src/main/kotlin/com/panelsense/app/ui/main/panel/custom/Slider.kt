package com.panelsense.app.ui.main.panel.custom

import android.view.MotionEvent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitVerticalDragOrCancellation
import androidx.compose.foundation.gestures.verticalDrag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun VerticalSlider(
    modifier: Modifier = Modifier,
    initOn: Boolean = true,
    upSideDown: Boolean = false,
    maxValue: Float = 100f,
    initValue: Float = 0f,
    colorFlow: Flow<Color> = flowOf(Color.White),
    onUpdateValue: (Float) -> Unit = {}
) {
    var isDragged by remember { mutableStateOf(false) }
    var on by remember { mutableStateOf(initOn) }
    val color by colorFlow.collectAsState(initial = Color.White)
    var sliderValue by remember { mutableFloatStateOf(initValue.coerceIn(0f, maxValue)) }

    val animatedColor by animateColorAsState(targetValue = color, label = color.toString())
    val animatedSlideValue by animateFloatAsState(
        targetValue = sliderValue,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
        label = "initValue"
    )

    LaunchedEffect(key1 = initValue * initOn.hashCode()) {
        if (!isDragged) {
            sliderValue = initValue
            on = initOn
        }
    }

    Box(modifier = modifier
        .run {
            if (upSideDown) rotate(180f) else this
        }
        .pointerInteropFilter {
            it.action == MotionEvent.ACTION_DOWN
        }
        .pointerInput(Unit) {
            awaitEachGesture {

                val down = awaitFirstDown(requireUnconsumed = false).also { it.consume() }
                val drag = awaitVerticalDragOrCancellation(down.id)?.also { it.consume() }
                isDragged = true
                handleSliderUpdate(down.position, maxValue) {
                    sliderValue = it
                    onUpdateValue(it)
                }

                if (drag != null) {
                    verticalDrag(drag.id) {
                        it.consume()
                        handleSliderUpdate(it.position, maxValue) {
                            sliderValue = it
                            onUpdateValue(it)
                        }
                    }
                }
                isDragged = false
            }
        }
        .aspectRatio(0.3f)) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            val cornerRadius = CornerRadius(size.width * 0.3f)
            val strokeWidth = cornerRadius.x * 0.1f
            val minHeight = size.minHeight

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
                    color = Color.Gray,
                    start = Offset(cornerRadius.x, offsetY + cornerRadius.y),
                    end = Offset(size.width - cornerRadius.x, offsetY + cornerRadius.y),
                    strokeWidth = strokeWidth * 1.5f,
                    cap = StrokeCap.Round,
                )
            }
        }
    }
}

private fun PointerInputScope.handleSliderUpdate(
    offset: Offset,
    maxValue: Float,
    onUpdateValue: (Float) -> Unit
) {
    val minHeight = size.minHeight
    val position = offset.y
    val newP =
        maxValue - ((position.minus(minHeight.div(2)) * maxValue) / size.height.minus(
            minHeight
        ))
    onUpdateValue(newP.coerceIn(0f, maxValue))
}

private val IntSize.minHeight
    get() = width * MIN_HEIGHT_RATIO
private val Size.minHeight
    get() = width * MIN_HEIGHT_RATIO

fun Color.getBackground(darkFactor: Float = 0.5f): Color = copy(
    alpha = alpha * darkFactor,
    red = red * darkFactor,
    green = green * darkFactor,
    blue = blue * darkFactor,
)

private const val MIN_HEIGHT_RATIO = 0.6f

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
