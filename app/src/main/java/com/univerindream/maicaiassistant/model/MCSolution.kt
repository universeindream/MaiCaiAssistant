package com.univerindream.maicaiassistant.model

import com.blankj.utilcode.util.TimeUtils
import com.univerindream.maicaiassistant.BuildConfig
import java.util.*

/**
 * 解决方案
 */
data class MCSolution(
    var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var author: String = "",
    var desc: String = "",
    var updateDateStr: String = TimeUtils.getNowString(),
    var appVersion: String = BuildConfig.VERSION_NAME,

    val steps: MutableList<MCStep> = arrayListOf(),
    var launchTime: Long = 25,
    var stepDelay: Long = 100,

    var failIsAlarm: Boolean = true,
    var failDuration: Long = 10000,
)
