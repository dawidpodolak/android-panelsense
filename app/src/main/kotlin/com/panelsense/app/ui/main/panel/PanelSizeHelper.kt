package com.panelsense.app.ui.main.panel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.dp

class PanelSizeHelper(
    private val remember: MutableState<LayoutCoordinates?>,
    private val heightToWidthRatio: Float
) {
    val orientation: PanelItemOrientation
        get() = calculateOrientation()

    val panelItemSize: PanelItemSize?
        get() = calculatePanelSize()

    fun onGlobalLayout(coordinates: LayoutCoordinates) {
        remember.value = coordinates
    }

    private fun calculatePanelSize(): PanelItemSize? {
        val width = remember.value?.size?.width ?: return null
        return if (width < 250.dp.value.toInt()) {
            PanelItemSize.SMALL
        } else {
            PanelItemSize.LARGE
        }
    }

    private fun calculateOrientation(): PanelItemOrientation {
        val intSize = remember.value?.size ?: return PanelItemOrientation.VERTICAL
        if (remember.value == null) return PanelItemOrientation.VERTICAL
        return if (intSize.width > intSize.height.times(heightToWidthRatio)) {
            PanelItemOrientation.HORIZONTAL
        } else {
            PanelItemOrientation.VERTICAL
        }
    }

    enum class PanelItemOrientation {
        HORIZONTAL,
        VERTICAL
    }

    enum class PanelItemSize {
        SMALL,
        LARGE
    }
}

@Composable
fun getPanelSizeHelper(heightToWidthRatio: Float = 2f): PanelSizeHelper =
    PanelSizeHelper(remember { mutableStateOf<LayoutCoordinates?>(null) }, heightToWidthRatio)
