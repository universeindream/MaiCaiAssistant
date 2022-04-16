package com.univerindream.maicaiassistant

import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson

object MHData {

    /**
     * 当前方案
     */
    var curMCSolutionJSON: String
        get() = SPUtils.getInstance()
            .getString("curMCSolutionJSON", Gson().toJson(MHDefault.defaultMCSolutions.first()))
        set(value) = if (value.isBlank()) SPUtils.getInstance().remove("curMCSolutionJSON") else SPUtils.getInstance()
            .put("curMCSolutionJSON", value)

    /**
     * 抢购时长
     */
    var buyMinTime: Int
        get() = SPUtils.getInstance().getInt("buyTime", 25)
        set(value) = SPUtils.getInstance().put("buyTime", value)

    /**
     * 定时抢购
     */
    var timerTriggerStatus: Boolean
        get() = SPUtils.getInstance().getBoolean("timerTriggerStatus")
        set(value) = SPUtils.getInstance().put("timerTriggerStatus", value)

    /**
     * 定时抢购时间
     */
    var timerTriggerTime: Long
        get() = SPUtils.getInstance().getLong("timerTriggerTime", -1)
        set(value) = SPUtils.getInstance().put("timerTriggerTime", value)

    /**
     * 错误响铃状态
     */
    var wrongAlarmStatus: Boolean
        get() = SPUtils.getInstance().getBoolean("wrongAlarmStatus", true)
        set(value) = SPUtils.getInstance().put("wrongAlarmStatus", value)

}