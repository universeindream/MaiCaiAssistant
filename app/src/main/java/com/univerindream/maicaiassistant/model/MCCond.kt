package com.univerindream.maicaiassistant.model

/**
 * 条件
 */
data class MCCond(
    var type: ECond = ECond.NODE_IS_MATCH,
    val node: MCNode = MCNode()
)
