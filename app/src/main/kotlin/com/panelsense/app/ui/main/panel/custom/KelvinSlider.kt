package com.panelsense.app.ui.main.panel.custom

import android.graphics.Paint
import android.view.MotionEvent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

val WarmTempColor = Color(0xFFFF850C)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun KelvinVerticalSlider(
    modifier: Modifier = Modifier,
    maxValue: Int = 6000,
    minValue: Int = 2000,
    initValue: Int? = null,
    onUpdateValue: (Int) -> Unit = {}
) {
    var isDragged by remember { mutableStateOf(false) }
    var parentSize by remember { mutableStateOf(IntSize.Zero) }
    var value by remember { mutableStateOf<Int?>(initValue) }
    val sliderHeight = parentSize.width * MIN_HEIGHT_RATIO

    val sliderPosition = if (value != null) {
        (value!! - minValue) * (parentSize.height - sliderHeight) / (maxValue - minValue)
    } else null

    LaunchedEffect(key1 = initValue) {
        if (!isDragged) {
            value = initValue
        }
    }

    val paint = Paint().apply {
        shader = LinearGradientShader(
            Offset.Zero,
            Offset(parentSize.width.toFloat(), parentSize.height.toFloat()),
            listOf(WarmTempColor, Color.White),
            null
        )
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
                value =
                    ((down.position.y / parentSize.height) * (maxValue - minValue) + minValue).roundToInt()
                onUpdateValue(value!!)
                if (drag != null) {
                    verticalDrag(drag.id) {
                        value =
                            ((it.position.y / parentSize.height) * (maxValue - minValue) + minValue)
                                .roundToInt()
                                .coerceIn(minValue, maxValue)
                        onUpdateValue(value!!)
                        it.consume()
                    }
                }
                isDragged = false
            }
        }
        .aspectRatio(0.3f)) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { parentSize = it }
        ) {
            val cornerRadius = CornerRadius(size.width * 0.3f)
            val strokeWidth = cornerRadius.x * 0.1f

            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawRoundRect(
                    0f,
                    0f,
                    size.width,
                    size.height,
                    cornerRadius.x,
                    cornerRadius.y,
                    paint
                )

            }

            if (sliderPosition != null) {
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(strokeWidth / 2, sliderPosition),
                    size = size.copy(
                        width = size.width - strokeWidth,
                        height = sliderHeight
                    ),
                    cornerRadius = cornerRadius,
                )

                drawLine(
                    color = Color.Gray,
                    start = Offset(cornerRadius.x, sliderPosition.plus(sliderHeight.div(2))),
                    end = Offset(
                        size.width - cornerRadius.x,
                        sliderPosition.plus(sliderHeight.div(2))
                    ),
                    strokeWidth = strokeWidth * 1.5f,
                    cap = StrokeCap.Round,
                )
            }
        }
    }
}

private const val MIN_HEIGHT_RATIO = 0.6f

@Preview(widthDp = 40, heightDp = 200)
@Composable()
fun KelvinVerticalSliderPreview() {
    KelvinVerticalSlider(
        minValue = 2000,
        maxValue = 6000,
        initValue = 6000,
    ) {

    }
}
