package com.univerindream.maicaiassistant

enum class EMCNodeType {
    ID,
    TXT,
    CLASSNAME,
    PACKAGE_NAME, ;

    fun toStr(): String {

        return when (this) {
            ID -> "ID"
            TXT -> "文本"
            CLASSNAME -> "类名"
            PACKAGE_NAME -> "包名"
        }
    }

    companion object {
        fun strOf(str: String): EMCNodeType {
            return when (str) {
                "ID" -> ID
                "文本" -> TXT
                "类名" -> CLASSNAME
                "包名" -> PACKAGE_NAME
                else -> throw Exception("EMCNodeType strOf error")
            }
        }
    }
}

/**
 * 条件
 */
enum class EMCCond {
    APP_IS_BACKGROUND,
    EQ_PACKAGE_NAME,
    EQ_CLASS_NAME,
    NODE_EXIST,
    NODE_NO_EXIST,
    NODE_VISIBLE,
    NODE_NO_VISIBLE,
    NODE_CAN_CLICK,
    NODE_NOT_CLICK,
    NODE_SELECTED,
    NODE_NOT_SELECTED,
    NODE_CHECKED,
    NODE_NOT_CHECKED;

    fun toStr(): String {

        return when (this) {
            APP_IS_BACKGROUND -> "软件在后台"
            EQ_PACKAGE_NAME -> "包名一致"
            EQ_CLASS_NAME -> "类名一致"
            NODE_EXIST -> "节点存在"
            NODE_NO_EXIST -> "节点不存在"
            NODE_VISIBLE -> "节点可见"
            NODE_NO_VISIBLE -> "节点不可见"
            NODE_CAN_CLICK -> "节点可点击"
            NODE_NOT_CLICK -> "节点不可点击"
            NODE_SELECTED -> "节点已选择"
            NODE_NOT_SELECTED -> "节点未选择"
            NODE_CHECKED -> "节点已选中"
            NODE_NOT_CHECKED -> "节点未选中"
        }
    }

    companion object {

        fun strOf(str: String): EMCCond {
            return when (str) {
                "软件在后台" -> APP_IS_BACKGROUND
                "包名一致" -> EQ_PACKAGE_NAME
                "类名一致" -> EQ_CLASS_NAME
                "节点存在" -> NODE_EXIST
                "节点不存在" -> NODE_NO_EXIST
                "节点可见" -> NODE_VISIBLE
                "节点不可见" -> NODE_NO_VISIBLE
                "节点可点击" -> NODE_CAN_CLICK
                "节点不可点击" -> NODE_NOT_CLICK
                "节点已选择" -> NODE_SELECTED
                "节点未选择" -> NODE_NOT_SELECTED
                "节点已选中" -> NODE_CHECKED
                "节点未选中" -> NODE_NOT_CHECKED
                else -> throw Exception("EMCCond strOf error")
            }
        }

    }
}


/**
 * 处理
 */
enum class EMCHandle {
    BACK,
    RECENTS,
    LAUNCH,
    CLICK_NODE,
    CLICK_NODE_JUST_SELF,
    CLICK_RANDOM_NODE,
    CLICK_RANDOM_NODE_JUST_SELF,
    NONE;

    fun toStr(): String {
        return when (this) {
            BACK -> "返回健"
            RECENTS -> "最近健"
            LAUNCH -> "运行软件"
            CLICK_NODE -> "点击控件"
            CLICK_NODE_JUST_SELF -> "点击控件(不包括父控件)"
            CLICK_RANDOM_NODE -> "点击随机控件"
            CLICK_RANDOM_NODE_JUST_SELF -> "点击随机控件(不包括父控件)"
            NONE -> "无动作"
        }
    }

    companion object {
        fun strOf(str: String): EMCHandle {
            return when (str) {
                "返回健" -> BACK
                "最近健" -> RECENTS
                "运行软件" -> LAUNCH
                "点击控件" -> CLICK_NODE
                "点击控件(不包括父控件)" -> CLICK_NODE_JUST_SELF
                "点击随机控件" -> CLICK_RANDOM_NODE
                "点击随机控件(不包括父控件)" -> CLICK_RANDOM_NODE_JUST_SELF
                "无动作" -> NONE
                else -> throw Exception("EMCHandle strOf error")
            }
        }
    }
}

/**
 * 匹配
 */
enum class EMCMatch {
    CLICKABLE,
    CLICKABLE_SELF_OR_PARENT,
    CLICKABLE_WITH_DISABLE,

    CHECKABLE,
    CHECKABLE_SELF_OR_BROTHER,
    CHECKABLE_WITH_DISABLE,

    SELECTED,
    SELECTED_SELF_OR_BROTHER,
    SELECTED_WITH_DISABLE,

    CHECKED,
    CHECKED_SELF_OR_BROTHER,
    CHECKED_WITH_DISABLE,

    VISIBLE,

    ALL
}

/**
 * 节点
 */
data class MCNode(
    val nodeType: EMCNodeType = EMCNodeType.TXT,
    val nodeKey: String = "",
    val nodeIndex: Int = 0,
    val className: String = "",
    val packageName: String = "",
)

/**
 * 解决方案
 */
data class MCSolution(
    var name: String = "",
    val steps: MutableList<MCStep>
)

/**
 * 步骤
 */
data class MCStep(
    var name: String = "",
    var condList: MutableList<MCCond> = arrayListOf(),
    var handle: MCHandle = MCHandle(),
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
)

/**
 * 条件
 */
data class MCCond(
    val type: EMCCond = EMCCond.NODE_EXIST,
    val node: MCNode = MCNode()
)

/**
 * 处理
 */
data class MCHandle(
    val type: EMCHandle = EMCHandle.NONE,
    val node: MCNode = MCNode(),
    val delayRunAfter: Long = 0,
    val delayRunBefore: Long = 0,
)

/**
 * 处理记录
 */
data class MCHandleLog(
    var stepName: String,
    var handleTime: Long
)
