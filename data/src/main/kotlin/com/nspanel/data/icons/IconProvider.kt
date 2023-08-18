package com.nspanel.data.icons

import android.graphics.drawable.Drawable
import com.nspanel.core.model.IconSpec
import kotlinx.coroutines.flow.Flow

interface IconProvider {
    fun getIcon(iconSpec: IconSpec): Flow<Drawable>
}
