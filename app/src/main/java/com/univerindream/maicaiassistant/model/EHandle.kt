package com.univerindream.maicaiassistant.model

/**
 * 操作类型
 */
enum class EHandle {
    CLICK_NODE,
    CLICK_SCREEN,
    LAUNCH,
    RECENTS,
    BACK,
    NONE,
    ;

    fun toStr(): String {
        return when (this) {
            CLICK_NODE -> "点击控件"
            CLICK_SCREEN -> "点击屏幕"
            LAUNCH -> "打开软件"
            RECENTS -> "最近健"
            BACK -> "返回健"
            NONE -> "无动作"
        }
    }

    companion object {
        fun strOf(str: String?): EHandle {
            return when (str) {
                "点击控件" -> CLICK_NODE
                "点击屏幕" -> CLICK_SCREEN
                "打开软件" -> LAUNCH
                "最近健" -> RECENTS
                "返回健" -> BACK
                else -> NONE
            }
        }
    }
}