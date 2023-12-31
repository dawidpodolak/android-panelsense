package com.panelsense.app

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import io.sentry.Sentry
import timber.log.Timber

@HiltAndroidApp
class PanelSenseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        Sentry.init(BuildConfig.SENTRY_DSN)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
