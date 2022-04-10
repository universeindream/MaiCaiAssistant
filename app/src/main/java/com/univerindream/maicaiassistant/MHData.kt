package com.univerindream.maicaiassistant

import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson

object MHData {

    /**
     * 当前方案
     */
    var curJsonMHSolution: String
        get() = SPUtils.getInstance()
            .getString("curJsonMHSolution", Gson().toJson(MHDefault.defaultMHSolutions.first()))
        set(value) = if (value.isBlank()) SPUtils.getInstance().remove("curJsonMHSolution") else SPUtils.getInstance()
            .put("curJsonMHSolution", value)

    /**
     * 所有方案
     */
    var allJsonMHSolution: String
        get() = SPUtils.getInstance()
            .getString("allJsonMHSolution", Gson().toJson(MHDefault.defaultMHSolutions))
        set(value) = if (value.isBlank()) SPUtils.getInstance().remove("allJsonMHSolution") else SPUtils.getInstance()
            .put("allJsonMHSolution", value)

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