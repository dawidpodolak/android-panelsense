package com.panelsense.app.ui.main.panel

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.semantics.Role
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

suspend fun EntityInteractor.getDrawable(
    mdiName: String?,
    enable: Boolean,
    enabledColor: Color? = null
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

suspend fun EntityInteractor.getDrawable(
    mdiName: String?,
    color: Color,
): Drawable? {

    val iconSpec = IconSpec(
        name = mdiName ?: return null,
        color = color.toArgb()
    )
    return getIconProvider().getIcon(iconSpec)
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.effectClickable(
    hapticFeedback: HapticFeedback? = null,
    viewSoundEffect: View? = null,
    interactionSource: MutableInteractionSource,
    indication: Indication?,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit,
) = combinedClickable(
    interactionSource = interactionSource,
    indication = indication,
    enabled = enabled,
    onClickLabel = onClickLabel,
    role = role,
    onLongClick = onLongClick,
    onClick = {
        onClick()
        viewSoundEffect?.playSoundEffect(SoundEffectConstants.CLICK)
        hapticFeedback?.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    },
)
