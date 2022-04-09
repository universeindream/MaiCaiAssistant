package com.univerindream.maicaiassistant

enum class EMCNodeType {
    ID, //ID 节点
    TXT, //内容节点
    Depth, //从根节点下砖次
}

/**
 * 条件
 */
enum class EMCCond {
    APP_IS_BACKGROUND, //应用后台
    EQ_CLASS_NAME, //类名一致
    NODE_EXIST, //节点存在
    NODE_NO_EXIST, //节点不存在
    NODE_IS_UNSELECTED, //节点必须已选择
    NODE_CAN_CLICK, //节点可点击
    NODE_NOT_CLICK, //节点不可点击
}


/**
 * 处理
 */
enum class EMCHandle {
    CLICK_NODE,//单个节点运行
    CLICK_SCOPE_RANDOM_NODE, //指定多个节点，随机运行
    BACK,//返回上一页
    LAUNCH,//运行 APP
}

/**
 * 节点
 */
data class MCNode(
    val nodeType: EMCNodeType? = null,
    val nodeKey: String? = null,
    val className: String? = null,
    val packageName: String? = null,
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

data class MCHandleLog(
    var stepName: String,
    var handleTime: Long
)
