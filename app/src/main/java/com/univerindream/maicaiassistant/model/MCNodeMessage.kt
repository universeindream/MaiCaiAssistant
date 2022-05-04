package com.univerindream.maicaiassistant.model

import android.view.accessibility.AccessibilityNodeInfo

data class MCNodeMessage(
    val name: String,
    val value: Any?,
    val node: AccessibilityNodeInfo? = null
)
