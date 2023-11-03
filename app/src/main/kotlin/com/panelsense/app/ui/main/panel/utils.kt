package com.panelsense.app.ui.main.panel

import android.content.Context
import android.graphics.drawable.Drawable
import android.media.AudioManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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

fun withSound(context: Context) {
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
    audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK)
}
