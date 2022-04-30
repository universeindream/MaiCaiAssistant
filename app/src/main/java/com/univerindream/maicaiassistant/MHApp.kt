package com.univerindream.maicaiassistant

import android.app.Application
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.formatter.border.DefaultBorderFormatter
import com.elvishew.xlog.internal.SystemCompat


class MHApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initCrash()
        initXLog()
    }

    private fun initCrash() {
        CaocConfig.Builder.create()
            .apply()
    }

    private fun initXLog() {
        XLog.init(
            LogConfiguration.Builder()
                .tag("MCHelp")
                .logLevel(if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.INFO)
                .borderFormatter(object : DefaultBorderFormatter() {
                    override fun format(segments: Array<out String>?): String {
                        return "⬇️" + SystemCompat.lineSeparator + super.format(segments)
                    }
                })
                .enableThreadInfo()
                .enableStackTrace(3)
                .enableBorder()
                .build()
        )
    }

}