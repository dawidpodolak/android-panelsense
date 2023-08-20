package com.nspanel.data.icons

import android.graphics.drawable.Drawable
import com.nspanel.core.model.icon.IconSpec

interface IconProvider {
    suspend fun getIcon(iconSpec: IconSpec): Drawable?
}
