package com.panelsense.app.ui.main.panel.custom

import android.graphics.ComposeShader
import android.graphics.Paint
import android.graphics.PorterDuff
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitDragOrCancellation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.SweepGradientShader
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.panelsense.domain.model.entity.state.LightEntityState
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


@Composable
fun CircleColorPickerView(
    modifier: Modifier = Modifier,
    lightEntityState: LightEntityState,
    onColorChanged: (Color) -> Unit
) {
    Box(modifier) {
        var offset by remember { mutableStateOf<Offset?>(null) }
        var isDragged by remember { mutableStateOf<Boolean>(false) }
        var size by remember { mutableStateOf(IntSize.Zero) }
        var center by remember { mutableStateOf(Offset.Zero) }
        var radius by remember { mutableStateOf(0f) }
        var selectedColor by remember { mutableStateOf(Color.White) }

        val colors = listOf(
            Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red
        )
        val shader = getShader(center, radius, colors)
        val sweepPaint = Paint().apply {
            this.shader = shader
        }

        LaunchedEffect(key1 = lightEntityState.rgbColor) {
            if (isDragged) return@LaunchedEffect
            selectedColor = lightEntityState.rgbColor!!.toComposeColor()
            offset = getOffsetFromColor(
                lightEntityState.rgbColor!!.toComposeColor(),
                Size(size.width.toFloat(), size.height.toFloat())
            )
        }

        Canvas(modifier = Modifier
            .fillMaxHeight()
            .onSizeChanged {
                center = Offset(it.width / 2f, it.height / 2f)
                radius = min(it.width, it.height) / 2f
                size = it
            }
            .aspectRatio(1f)
            .pointerInput(null) {
                awaitEachGesture {
                    val down = awaitFirstDown().also { it.consume() }
                    offset = limitOffsetToCircle(center, radius, down.position)
                    selectedColor = getColorFromGradient(center, radius, offset!!, colors)
                    onColorChanged(selectedColor)
                    isDragged = true
                    val upOrCancel = awaitDragOrCancellation(down.id)
                    if (upOrCancel != null) {
                        drag(upOrCancel.id) {
                            offset = limitOffsetToCircle(center, radius, it.position)
                            selectedColor = getColorFromGradient(center, radius, offset!!, colors)
                            onColorChanged(selectedColor)
                            it.consume()
                        }
                    }
                    isDragged = false
                }
            }
        ) {
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawCircle(
                    center.x,
                    center.y,
                    radius,
                    sweepPaint
                )
            }

            if (offset != null) {
                drawCircle(
                    color = selectedColor,
                    center = offset!!,
                    radius = 12.dp.toPx(),
                    style = Fill
                )
                drawCircle(
                    color = Color.White,
                    center = offset!!,
                    radius = 12.dp.toPx(),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }
    }
}

@Composable
private fun getShader(
    center: Offset,
    radius: Float,
    colors: List<Color>
): ComposeShader {

    val sweepShader = SweepGradientShader(
        center = center,
        colors = colors,
        colorStops = null
    )

    val radialGradient = RadialGradientShader(
        center = center,
        radius = radius.takeIf { it > 0f } ?: 1f,
        colors = listOf(Color.White, Color.Transparent),
        colorStops = null
    )
    return ComposeShader(sweepShader, radialGradient, PorterDuff.Mode.SRC_OVER)
}

fun LightEntityState.Color.toComposeColor(): Color {
    return Color(red = red, green = green, blue = blue)
}

fun limitOffsetToCircle(center: Offset, radius: Float, tapOffset: Offset): Offset {
    val dx = tapOffset.x - center.x
    val dy = tapOffset.y - center.y
    val distanceFromCenter = sqrt(dx * dx + dy * dy)

    // If the tap is inside the circle, return it unmodified
    if (distanceFromCenter <= radius) {
        return tapOffset
    }

    // If the tap is outside the circle, calculate the angle and project the tap onto the circle's circumference
    val angle = atan2(dy, dx)
    val limitedX = center.x + radius * cos(angle)
    val limitedY = center.y + radius * sin(angle)
    return Offset(limitedX, limitedY)
}


fun getColorFromGradient(
    center: Offset,
    radius: Float,
    tapOffset: Offset,
    colors: List<Color>
): Color {
    // Calculate angle for the sweep gradient
    val angle = atan2(center.y - tapOffset.y, center.x - tapOffset.x) + Math.PI
    val sweepPosition = angle / (2 * Math.PI) // Normalize to [0, 1]

    // Determine which colors to interpolate between
    val sweepIndex = (sweepPosition * (colors.size - 1)).toInt()
    val sweepColorStart = colors[sweepIndex]
    val sweepColorEnd = colors[(sweepIndex + 1) % colors.size]

    // Interpolate between the two colors based on the exact position
    val sweepInterpolation = ((sweepPosition * (colors.size - 1)) % 1).toFloat()
    val sweepColor = lerp(sweepColorStart, sweepColorEnd, sweepInterpolation)

    // Calculate distance for the radial gradient
    val distance = tapOffset.getDistance(center) / radius
    val radialFactor = distance.coerceIn(0f, 1f)

    // Interpolate between white (center) and the color from the sweep gradient
    return lerp(Color.White, sweepColor, radialFactor)
}

fun lerp(startColor: Color, endColor: Color, fraction: Float): Color {
    return Color(
        lerp(startColor.red, endColor.red, fraction),
        lerp(startColor.green, endColor.green, fraction),
        lerp(startColor.blue, endColor.blue, fraction),
        1f // Assume fully opaque colors
    )
}

fun lerp(start: Float, end: Float, fraction: Float): Float {
    return (start + (end - start) * fraction)
}

fun Offset.getDistance(other: Offset): Float {
    return sqrt((x - other.x).pow(2) + (y - other.y).pow(2))
}

@Suppress("MagicNumber")
fun getOffsetFromColor(color: Color, size: Size): Offset {
    // Step 1: Convert to HSB (this step requires a specific implementation to convert Color to HSB)
    val (hue, saturation, brightness) = color.toHsb()
    // Step 2: Map hue to angle
    val angle = hue * (2 * Math.PI) / 360.0

    // Step 3: Determine the distance from the center based on saturation and brightness
    // This is an oversimplified example and likely needs to be adjusted
    val distanceFromCenter = size.minDimension / 2 * saturation * brightness

    // Step 4: Convert polar coordinates to Cartesian coordinates
    val x = size.width / 2 + distanceFromCenter * cos(angle)
    val y = size.height / 2 + distanceFromCenter * sin(angle)

    return Offset(x.toFloat(), y.toFloat())
}

@Suppress("MagicNumber")
fun Color.toHsb(): Triple<Float, Float, Float> {
    // Extract RGB components from 0 to 1
    val r = this.red
    val g = this.green
    val b = this.blue

    // Calculate min and max RGB components
    val cMax = max(r, max(g, b))
    val cMin = min(r, min(g, b))
    val delta = cMax - cMin

    // Calculate Hue
    val hue: Float = when {
        delta == 0f -> 0f
        cMax == r -> ((g - b) / delta) % 6f
        cMax == g -> ((b - r) / delta) + 2f
        else -> ((r - g) / delta) + 4f
    } * 60f

    // Calculate Saturation
    val saturation = if (cMax == 0f) 0f else (delta / cMax)

    // Calculate Brightness
    val brightness = cMax

    // Return the HSB values
    return Triple(
        if (hue < 0f) hue + 360f else hue,
        saturation,
        brightness
    )
}


@Preview(widthDp = 300, heightDp = 300)
@Composable
fun CircleColorPickerPreview() {
//    CircleColorPickerView(onColorChanged = {})
}
