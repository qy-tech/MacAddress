package com.qytech.systeminforeader

import android.app.Application
import timber.log.Timber

class SystemInfoReaderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}