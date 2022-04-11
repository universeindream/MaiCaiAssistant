package com.univerindream.maicaiassistant

import android.app.Application
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.TimeUtils
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.flattener.DefaultFlattener
import com.elvishew.xlog.formatter.border.DefaultBorderFormatter
import com.elvishew.xlog.internal.SystemCompat
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator


class MHApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initCrash()
        initXLog()

        if (BuildConfig.DEBUG) {
            MHData.curJsonMHSolution = ""
            MHData.allJsonMHSolution = ""
        }
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
                .enableStackTrace(3)
                .enableBorder()
                .build(),
            AndroidPrinter(true),
            FilePrinter.Builder(PathUtils.join(PathUtils.getExternalAppCachePath(), "logs"))
                .fileNameGenerator(DateFileNameGenerator())
                .backupStrategy(NeverBackupStrategy())
                .cleanStrategy(FileLastModifiedCleanStrategy(1000 * 60 * 60 * 24))
                .flattener(object : DefaultFlattener() {
                    override fun flatten(
                        timeMillis: Long,
                        logLevel: Int,
                        tag: String?,
                        message: String?
                    ): CharSequence {
                        return (TimeUtils.millis2String(timeMillis)
                                + '|' + LogLevel.getShortLevelName(logLevel)
                                + '|' + tag
                                + '|' + message)
                    }
                })
                .build()
        )
    }

}