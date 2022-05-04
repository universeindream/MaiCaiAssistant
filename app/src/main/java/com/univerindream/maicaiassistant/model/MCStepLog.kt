package com.univerindream.maicaiassistant.model

data class MCStepLog(
    val index: Int,
    val step: MCStep,
    var executionTime: Long = 0L
)
