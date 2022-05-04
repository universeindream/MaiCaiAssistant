package com.univerindream.maicaiassistant.model

/**
 * 节点
 */
data class MCNode(
    var packageName: String = "",
    var className: String = "",
    var id: String = "",
    var txt: String = "",
    var index: Int = 0,
    var coordinate: String = "",

    var isEnabled: Boolean? = null,
    var isFocused: Boolean? = null,
    var isChecked: Boolean? = null,
    var isSelected: Boolean? = null,
    var isVisibleToUser: Boolean? = null,

    var isClickable: Boolean? = null,
    var isCheckable: Boolean? = null,
    var isEditable: Boolean? = null,
    var isScrollable: Boolean? = null,
)
