package com.univerindream.maicaiassistant.model

/**
 * 操作
 */
data class MCHandle(
    var type: EHandle = EHandle.NONE,
    val node: MCNode = MCNode(),
    var delayRunAfter: Long = 0,
    var delayRunBefore: Long = 100,
)