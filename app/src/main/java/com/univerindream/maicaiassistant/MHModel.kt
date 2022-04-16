package com.univerindream.maicaiassistant

enum class EMCNodeType {
    ID, //ID 节点
    TXT, //内容节点
    CLASSNAME,
    PACKAGE_NAME, ;

    fun to2String(): String {

        return when (this) {
            ID -> "ID"
            TXT -> "文本"
            CLASSNAME -> "类名"
            PACKAGE_NAME -> "包名"
        }
    }
}

/**
 * 条件
 */
enum class EMCCond {
    APP_IS_BACKGROUND, //应用后台
    EQ_CLASS_NAME, //类名一致
    NODE_EXIST, //节点存在
    NODE_NO_EXIST, //节点不存在
    NODE_VISIBLE, //节点可见
    NODE_NO_VISIBLE, //节点不可见
    NODE_CAN_CLICK, //节点可点击
    NODE_NOT_CLICK, //节点不可点击
    NODE_SELECTED, //节点已选择
    NODE_NOT_SELECTED, //节点未选择
    NODE_CHECKED, //节点必须已选择
    NODE_NOT_CHECKED; //节点必须已选择

    fun to2String(): String {

        return when (this) {
            APP_IS_BACKGROUND -> "App 在后台"
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

    fun to2String(): String {
        return when (this) {
            BACK -> "返回健"
            RECENTS -> "最近健"
            LAUNCH -> "运行软件"
            CLICK_NODE -> "点击控件"
            CLICK_NODE_JUST_SELF -> "点击控件自身"
            CLICK_RANDOM_NODE -> "点击随机控件"
            CLICK_RANDOM_NODE_JUST_SELF -> "点击随机控件自身"
            NONE -> "无动作"
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
    val nodeType: EMCNodeType,
    val nodeKey: String = "",
    val nodeIndex: Int = 0,
    val className: String = "",
    val packageName: String = "",
)

/**
 * 解决方案
 */
data class MCSolution(
    val name: String,
    val steps: MutableList<MCStep>
)

/**
 * 步骤
 */
data class MCStep(
    var name: String,
    var condList: MutableList<MCCond> = arrayListOf(),
    var handle: MCHandle,
    /** 是否警报 **/
    var isAlarm: Boolean = false,
    /** 是否手动 **/
    var isManual: Boolean = false
)

/**
 * 条件
 */
data class MCCond(
    val type: EMCCond,
    val node: MCNode
)

/**
 * 处理
 */
data class MCHandle(
    val type: EMCHandle,
    val node: MCNode,
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
