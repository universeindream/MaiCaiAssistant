package com.univerindream.maicaiassistant

import android.app.Application
import cat.ereza.customactivityoncrash.config.CaocConfig
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MCApp : Application() {

    override fun onCreate() {
        super.onCreate()

        CaocConfig.Builder.create().apply()
    }

}