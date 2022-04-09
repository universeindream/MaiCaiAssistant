package com.univerindream.maicaiassistant

import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson

object MHData {

    /**
     * 抢购平台
     */
    var buyPlatform: Int
        get() = SPUtils.getInstance().getInt("buyPlatform", 1)
        set(value) = SPUtils.getInstance().put("buyPlatform", value)

    var jsonSteps: String
        get() = if (buyPlatform == 1) meiTuanJsonSteps else dingDongJsonSteps
        set(value) {
            if (buyPlatform == 1) {
                meiTuanJsonSteps = value
            } else {
                dingDongJsonSteps = value
            }
        }

    /**
     * 美团抢购流程
     */
    private var meiTuanJsonSteps: String
        get() = SPUtils.getInstance().getString("meiTuanJsonSteps", Gson().toJson(MHConfig.defaultMeiTuanSteps))
        set(value) = if (value.isBlank()) SPUtils.getInstance().remove("meiTuanJsonSteps") else SPUtils.getInstance()
            .put("meiTuanJsonSteps", value)

    /**
     * 叮咚抢购流程
     */
    private var dingDongJsonSteps: String
        get() = SPUtils.getInstance().getString("dingDongJsonSteps", Gson().toJson(MHConfig.defaultDingDongSteps))
        set(value) = if (value.isBlank()) SPUtils.getInstance().remove("dingDongJsonSteps") else SPUtils.getInstance()
            .put("dingDongJsonSteps", value)

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