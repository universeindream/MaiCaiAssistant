package com.univerindream.maicaiassistant.model

/**
 * 步骤
 */
data class MCStep(
    var name: String = "",
    var condList: MutableList<MCCond> = arrayListOf(),
    var handle: MCHandle = MCHandle(),
    /** 是否启用 **/
    var isEnable: Boolean = true,
    /** 是否警报 **/
    var isAlarm: Boolean = false,
    /** 是否手动 **/
    var isManual: Boolean = false,
    /** 是否重复 **/
    var isRepeat: Boolean = false,
    /** 是否失败后返回健 **/
    var isFailBack: Boolean = false,
    /** 是否失败后返回次数 **/
    var failBackCount: Int = 1,
    /** 是否执行一次 **/
    var isExecuteOnce: Boolean = false,
)
