package com.panelsense.core

import android.os.Build
import android.provider.Settings.Secure.ANDROID_ID
import androidx.annotation.RequiresApi
import javax.inject.Inject

class AppDataProvider @Inject constructor() {
    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    fun installationId(): String = ANDROID_ID
}
