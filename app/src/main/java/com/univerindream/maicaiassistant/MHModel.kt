package com.univerindream.maicaiassistant

enum class EMCSearch {
    ID, //ID 节点
    TXT, //内容节点
    CLASSNAME,
    PACKAGE_NAME,
}

/**
 * 条件
 */
enum class EMCCond {
    APP_IS_BACKGROUND, //应用后台
    EQ_CLASS_NAME, //类名一致
    NODE_EXIST, //节点存在
    NODE_NO_EXIST, //节点不存在
    NODE_VISIBLE, //节点存在
    NODE_CAN_CLICK, //节点可点击
    NODE_NOT_CLICK, //节点不可点击
    NODE_SELECTED, //节点必须已选择
    NODE_NOT_SELECTED, //节点必须已选择
    NODE_CHECKED, //节点必须已选择
    NODE_NOT_CHECKED, //节点必须已选择
}


/**
 * 处理
 */
enum class EMCHandle {
    BACK,//返回上一页
    RECENTS,//打开最近
    LAUNCH,//运行 APP
    CLICK_NODE,//点击节点
    CLICK_MULTIPLE_NODE,//点击多节点
    CLICK_SCOPE_RANDOM_NODE, //指定多个节点，随机运行
    CLICK_RANDOM_NODE, //随机点击
    NONE
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
    val search: EMCSearch,
    val nodeKey: String = "",
    val className: String = "",
    val packageName: String = "",
)

/**
 * 解决方案
 */
data class MHSolution(
    val name: String,
    val steps: List<MCStep>
)

/**
 * 步骤
 */
data class MCStep(
    val name: String,
    val condList: List<MCCond> = arrayListOf(),
    val handle: MCHandle,
    /** 是否警报 **/
    val isAlarm: Boolean = false,
    /** 是否手动 **/
    val isManual: Boolean = false
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
    val delay: Long = 0,
    val nodes: List<MCNode>
)

/**
 * 处理记录
 */
data class MCHandleLog(
    var stepName: String,
    var handleTime: Long
)
