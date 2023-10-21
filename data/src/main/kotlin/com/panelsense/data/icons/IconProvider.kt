package com.panelsense.data.icons

import android.graphics.drawable.Drawable
import com.panelsense.core.model.icon.IconSpec

interface IconProvider {
    suspend fun getIcon(iconSpec: IconSpec): Drawable?
}
