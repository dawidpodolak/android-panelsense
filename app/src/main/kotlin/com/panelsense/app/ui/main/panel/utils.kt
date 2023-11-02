package com.panelsense.app.ui.main.panel

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.IntSize
import androidx.core.content.ContextCompat
import com.panelsense.app.R
import com.panelsense.app.ui.main.EntityInteractor
import com.panelsense.app.ui.theme.StateDisabledColor
import com.panelsense.app.ui.theme.StateEnabledColor
import com.panelsense.core.model.icon.IconSpec
import com.panelsense.data.icons.IconProvider
import com.panelsense.domain.model.entity.command.EntityCommand
import com.panelsense.domain.model.entity.state.EntityState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.reflect.KClass

fun mockEntityInteractor(context: Context): EntityInteractor = object : EntityInteractor {
    override fun <T : EntityState> listenOnState(entityId: String, kType: KClass<T>): Flow<T> =
        flow { }

    override fun sendCommand(command: EntityCommand) = Unit

    override fun getIconProvider(): IconProvider = object : IconProvider {
        override suspend fun getIcon(iconSpec: IconSpec): Drawable? =
            ContextCompat.getDrawable(context, R.drawable.ic_launcher_background)
    }
}

enum class PanelItemOrientation {
    HORIZONTAL,
    VERTICAL
}

@Composable
fun getOrientationHelper(): OrientationHelper =
    OrientationHelper(remember { mutableStateOf(IntSize.Zero) })

class OrientationHelper(private val remember: MutableState<IntSize>) {
    val orientation: PanelItemOrientation
        get() = calculateOrientation()

    fun onSizeChanged(size: IntSize) {
        remember.value = size
    }

    private fun calculateOrientation(): PanelItemOrientation {
        return if (remember.value.width > remember.value.height.times(2)) {
            PanelItemOrientation.HORIZONTAL
        } else {
            PanelItemOrientation.VERTICAL
        }
    }
}

suspend fun EntityInteractor.getDrawable(
    mdiName: String?,
    enable: Boolean,
    enabledColor: androidx.compose.ui.graphics.Color? = null
): Drawable? {

    val iconSpec = IconSpec(
        name = mdiName ?: return null,
        color = when {
            enable && enabledColor != null -> enabledColor.toArgb()
            enable && enabledColor == null -> StateEnabledColor.toArgb()
            else -> StateDisabledColor.toArgb()
        }
    )
    return getIconProvider().getIcon(iconSpec)
}
