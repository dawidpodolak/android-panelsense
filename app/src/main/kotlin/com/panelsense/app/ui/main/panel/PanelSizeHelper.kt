package com.panelsense.app.ui.main.panel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.dp
import timber.log.Timber

class PanelSizeHelper(private val remember: MutableState<LayoutCoordinates?>) {
    val orientation: PanelItemOrientation
        get() = calculateOrientation()

    val panelItemSize: PanelItemSize?
        get() = calculatePanelSize()

    fun onGlobalLayout(coordinates: LayoutCoordinates) {
        Timber.d("onGlobalLayout: ${coordinates.size}")
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
        Timber.d("calculateOrientation: ${remember.value?.size}")
        val intSize = remember.value?.size ?: return PanelItemOrientation.VERTICAL
        if (remember.value == null) return PanelItemOrientation.VERTICAL
        return if (intSize.width > intSize.height.times(2)) {
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
fun getPanelSizeHelper(): PanelSizeHelper =
    PanelSizeHelper(remember { mutableStateOf<LayoutCoordinates?>(null) })
