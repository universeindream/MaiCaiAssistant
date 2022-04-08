package com.univerindream.maicaiassistant

import com.blankj.utilcode.util.SPUtils

object MHData {

    /**
     * 抢购平台
     */
    var buyPlatform: Int
        get() = SPUtils.getInstance().getInt("buyPlatform", 1)
        set(value) = SPUtils.getInstance().put("buyPlatform", value)

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
        get() = SPUtils.getInstance().getBoolean("timerTriggerStatus") && timerTriggerTime > System.currentTimeMillis()
        set(value) = SPUtils.getInstance().put("timerTriggerStatus", value)

    /**
     * 定时抢购时间
     */
    var timerTriggerTime: Long
        get() = SPUtils.getInstance().getLong("timerTriggerTime", -1)
        set(value) = SPUtils.getInstance().put("timerTriggerTime", value)


    /**
     * 支付响铃状态
     */
    var sendTimeSelectAlarmStatus: Boolean
        get() = SPUtils.getInstance().getBoolean("sendTimeSelectAlarmStatus", true)
        set(value) = SPUtils.getInstance().put("sendTimeSelectAlarmStatus", value)

    /**
     * 错误响铃状态
     */
    var wrongAlarmStatus: Boolean
        get() = SPUtils.getInstance().getBoolean("wrongAlarmStatus", true)
        set(value) = SPUtils.getInstance().put("wrongAlarmStatus", value)

}