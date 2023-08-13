package com.nspanel.sense

import android.app.Application
import timber.log.Timber

class NSPanelSenseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
