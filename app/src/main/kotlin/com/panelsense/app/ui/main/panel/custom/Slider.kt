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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun VerticalSlider(
    modifier: Modifier = Modifier,
    initOn: Boolean = true,
    upSideDown: Boolean = false,
    aspectRatioOn: Boolean = true,
    maxValue: Float = 100f,
    initValue: Float = 0f,
    colorFlow: Flow<Color> = flowOf(Color.White),
    onUpdateValue: (Float, SliderMotion) -> Unit = { _, _ -> }
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
        .pointerInteropFilter {
            it.action == MotionEvent.ACTION_DOWN
        }
        .pointerInput(Unit) {
            awaitEachGesture {

                val down = awaitFirstDown(requireUnconsumed = false).also { it.consume() }
                val drag = awaitVerticalDragOrCancellation(down.id)?.also { it.consume() }

                isDragged = true
                handleSliderUpdate(down.position, maxValue, upSideDown) {
                    sliderValue = it
                    onUpdateValue(it, SliderMotion.DOWN)
                }
                var lastDragPosition: Offset = down.position
                if (drag != null) {
                    verticalDrag(drag.id) {
                        it.consume()
                        lastDragPosition = it.position
                        handleSliderUpdate(it.position, maxValue, upSideDown) {
                            sliderValue = it
                            onUpdateValue(it, SliderMotion.DRAG)
                        }
                    }
                }
                isDragged = false
                handleSliderUpdate(lastDragPosition, maxValue, upSideDown) {
                    sliderValue = it
                    onUpdateValue(it, SliderMotion.UP)
                }
            }
        }
        .run {
            if (aspectRatioOn) aspectRatio(0.3f) else this
        }) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            val cornerRadius = CornerRadius((size.width * 0.3f).coerceAtMost(15.dp.toPx()))
            val strokeWidth = cornerRadius.x * 0.1f
            val minHeight = 40.dp.toPx()

            val maxHeight = size.height - strokeWidth - minHeight
            val requiredHeight = minHeight + (maxHeight * (animatedSlideValue / maxValue))
            val bottom = size.height - strokeWidth / 2
            val offsetY = if (upSideDown) {
                0f
            } else {
                strokeWidth / 2 + (bottom - requiredHeight)
            }
            val stripY = if (upSideDown) {
                requiredHeight - cornerRadius.x
            } else {
                offsetY + cornerRadius.y
            }

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
                    start = Offset(cornerRadius.x, stripY),
                    end = Offset(size.width - cornerRadius.x, stripY),
                    strokeWidth = strokeWidth * 1.5f,
                    cap = StrokeCap.Round,
                )
            }
        }
    }
}

enum class SliderMotion {
    DOWN, DRAG, UP
}

private fun PointerInputScope.handleSliderUpdate(
    offset: Offset,
    maxValue: Float,
    upSideDown: Boolean,
    onUpdateValue: (Float) -> Unit
) {
    val minHeight = 40.dp.toPx()
    val yPosition = offset.y
    val calculatedPosition = if (upSideDown) {
        maxValue - ((yPosition.minus(minHeight) * maxValue) / size.height.minus(
            minHeight
        ))
    } else {
        maxValue - ((yPosition.minus(minHeight.div(2)) * maxValue) / size.height.minus(
            minHeight
        ))
    }
    val finalPosition = if (upSideDown) {
        maxValue - calculatedPosition.coerceIn(0f, maxValue)
    } else {
        calculatedPosition.coerceIn(0f, maxValue)
    }
    onUpdateValue(finalPosition)
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

@Preview(widthDp = 200, heightDp = 200)
@Composable()
fun Preview() {
    VerticalSlider(
        initValue = 50f,
        maxValue = 250f,
        aspectRatioOn = false,
        initOn = true,
        upSideDown = true
    )
}
