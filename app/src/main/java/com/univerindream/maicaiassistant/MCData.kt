package com.univerindream.maicaiassistant

import com.blankj.utilcode.util.SPUtils

object MCData {

    /**
     * 当前方案
     */
    var curSolutionId: String
        get() = SPUtils.getInstance()
            .getString("curSolutionId", MCFile.firstSolution().id)
        set(value) = if (value.isBlank()) SPUtils.getInstance().remove("curSolutionId") else SPUtils.getInstance()
            .put("curSolutionId", value)

    /**
     * 是否提示过，分享作者
     */
    var isHintedShareAuthor: Boolean
        get() = SPUtils.getInstance().getBoolean("isHintShareAuthor")
        set(value) = SPUtils.getInstance().put("isHintShareAuthor", value)

    /**
     * 定时抢购
     */
    var timerStatus: Boolean
        get() = SPUtils.getInstance().getBoolean("timerStatus")
        set(value) = SPUtils.getInstance().put("timerStatus", value)

    /**
     * 定时抢购时间
     */
    var timerTime: Long
        get() = SPUtils.getInstance().getLong("timerTime", System.currentTimeMillis())
        set(value) = SPUtils.getInstance().put("timerTime", value)

}