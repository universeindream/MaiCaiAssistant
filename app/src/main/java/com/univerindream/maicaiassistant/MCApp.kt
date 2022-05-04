package com.univerindream.maicaiassistant

import android.app.Application
import androidx.work.Configuration
import cat.ereza.customactivityoncrash.config.CaocConfig
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


@HiltAndroidApp
class MCApp : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()

        CaocConfig.Builder.create().apply()
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(if (BuildConfig.DEBUG) android.util.Log.DEBUG else android.util.Log.ERROR)
            .build()

}