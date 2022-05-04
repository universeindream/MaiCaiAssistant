package com.univerindream.maicaiassistant.model

/**
 * 条件类型
 */
enum class ECond {
    APP_IS_BACKGROUND,
    APP_IS_EQUAL,
    PAGE_IS_EQUAL,
    NODE_IS_MATCH,
    NODE_NO_MATCH,
    ;

    fun toStr(): String {

        return when (this) {
            APP_IS_BACKGROUND -> "软件后台"
            APP_IS_EQUAL -> "软件一致"
            PAGE_IS_EQUAL -> "页面一致"
            NODE_IS_MATCH -> "控件存在"
            NODE_NO_MATCH -> "控件不存在"
        }
    }

    companion object {

        fun strOf(str: String?): ECond {
            return when (str) {
                "软件在后台" -> APP_IS_BACKGROUND
                "软件一致" -> APP_IS_EQUAL
                "页面一致" -> PAGE_IS_EQUAL
                "控件存在" -> NODE_IS_MATCH
                "控件不存在" -> NODE_NO_MATCH
                else -> NODE_IS_MATCH
            }
        }

    }
}