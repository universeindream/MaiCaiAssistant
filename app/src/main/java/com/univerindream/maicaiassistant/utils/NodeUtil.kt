package com.univerindream.maicaiassistant.utils

import android.os.Build
import android.view.accessibility.AccessibilityNodeInfo
import cn.hutool.json.JSONConfig
import cn.hutool.json.JSONUtil
import com.elvishew.xlog.XLog
import com.univerindream.maicaiassistant.EMCMatch

object NodeUtil {

    fun isExistById(root: AccessibilityNodeInfo?, id: String): Boolean =
        getByFirstId(root, id) != null

    fun isExistByTxt(root: AccessibilityNodeInfo?, txt: String): Boolean =
        getByFirstTxt(root, txt) != null

    fun matchByFirstId(
        root: AccessibilityNodeInfo?, id: String, match: EMCMatch,
        upwards: Int = 10000
    ): Boolean =
        getFirstAttrMatchNode(getByFirstId(root, id), match, upwards) != null

    fun matchByFirstTxt(
        root: AccessibilityNodeInfo?, txt: String, match: EMCMatch,
        upwards: Int = 10000
    ) =
        getFirstAttrMatchNode(getByFirstTxt(root, txt), match, upwards) != null

    fun matchInParentByFirstId(
        root: AccessibilityNodeInfo?, id: String, match: EMCMatch
    ) =
        getFirstAttrMatchInParentNode(getByFirstId(root, id), match) != null

    fun matchInParentByFirstTxt(
        root: AccessibilityNodeInfo?, txt: String, match: EMCMatch
    ) =
        getFirstAttrMatchInParentNode(getByFirstTxt(root, txt), match) != null

    /**
     * 是否可以点击 - 任意一个匹配 txt 节点
     */
    fun isClickById(root: AccessibilityNodeInfo?, id: String): Boolean {
        if (root == null) return false

        val data = root.findAccessibilityNodeInfosByViewId(id)
        data.forEach {
            if (getFirstAttrMatchNode(it, EMCMatch.CLICKABLE) != null) return true
        }

        return false
    }

    /**
     * 是否可以点击 - 任意一个匹配 txt 节点
     */
    fun isClickByTxt(root: AccessibilityNodeInfo?, txt: String): Boolean {
        if (root == null) return false

        val data = root.findAccessibilityNodeInfosByText(txt)
        data.forEach {
            if (getFirstAttrMatchNode(it, EMCMatch.CLICKABLE) != null) return true
        }

        return false
    }


    /**
     * 属性是否匹配
     */
    fun isAttributesMatch(info: AccessibilityNodeInfo?, match: EMCMatch): Boolean {
        if (info == null) return false

        return when (match) {
            EMCMatch.CLICKABLE -> info.isClickable && info.isEnabled
            EMCMatch.CLICKABLE_WITH_DISABLE -> info.isClickable

            EMCMatch.CHECKABLE -> info.isCheckable && info.isEnabled
            EMCMatch.CHECKABLE_WITH_DISABLE -> info.isCheckable

            EMCMatch.SELECTED -> info.isSelected && info.isEnabled
            EMCMatch.SELECTED_WITH_DISABLE -> info.isSelected

            EMCMatch.CHECKED -> info.isChecked && info.isEnabled
            EMCMatch.CHECKED_WITH_DISABLE -> info.isChecked
        }
    }

    /**
     * 获取节点 - 首个 id 匹配的节点
     */
    fun getByFirstId(root: AccessibilityNodeInfo?, id: String): AccessibilityNodeInfo? =
        root?.findAccessibilityNodeInfosByViewId(id)?.getOrNull(0)

    /**
     * 获取节点 - 首个 txt 匹配的节点
     */
    fun getByFirstTxt(root: AccessibilityNodeInfo?, txt: String): AccessibilityNodeInfo? =
        root?.findAccessibilityNodeInfosByText(txt)?.getOrNull(0)

    /**
     * 获取第一个匹配节点
     */
    fun getFirstAttrMatchNode(
        root: AccessibilityNodeInfo?,
        match: EMCMatch,
        upwards: Int = 10000
    ): AccessibilityNodeInfo? = getAllAttrMatchNode(root, match, upwards, true).firstOrNull()

    /**
     * 获取第一个匹配的父节点内的节点
     */
    fun getFirstAttrMatchInParentNode(
        root: AccessibilityNodeInfo?,
        match: EMCMatch
    ): AccessibilityNodeInfo? {
        root ?: return null
        val parent = root.parent ?: return null

        for (index in 0 until parent.childCount) {
            val curNode = parent.getChild(index)
            if (isAttributesMatch(curNode, match)) return curNode
        }

        return null
    }

    /**
     * 获取所有匹配节点
     */
    fun getAllAttrMatchNode(
        root: AccessibilityNodeInfo?,
        match: EMCMatch,
        upwards: Int = 10000, //向上遍历数
        firstMatch: Boolean = false, //第一个匹配
    ): List<AccessibilityNodeInfo> {
        val result = arrayListOf<AccessibilityNodeInfo>()
        if (root == null) return result

        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)

        var curUpwards = 0
        while (!queue.isEmpty()) {
            val node = queue.removeLast()

            if (isAttributesMatch(node, match)) {
                result.add(node)
                if (firstMatch) return result
            }

            if (curUpwards > upwards) return result

            node.parent?.let {
                queue.addFirst(it)
                curUpwards++
            }

        }

        return result
    }

    /**
     * 点击节点
     */
    fun click(
        root: AccessibilityNodeInfo?, match: EMCMatch = EMCMatch.CLICKABLE
    ): Boolean {
        if (root == null) return false
        if (isAttributesMatch(root, match)) return root.performAction(AccessibilityNodeInfo.ACTION_CLICK)

        val node = getFirstAttrMatchNode(root, match)
        return node?.performAction(AccessibilityNodeInfo.ACTION_CLICK) ?: false
    }

    /**
     * 点击第一个节点 - 首个 id 匹配的节点
     */
    fun clickByFirstId(root: AccessibilityNodeInfo?, id: String, match: EMCMatch = EMCMatch.CLICKABLE): Boolean =
        getByFirstId(root, id)?.let {
            click(it, match)
        } ?: false

    /**
     * 点击第一个节点 - 首个 txt 匹配的节点
     */
    fun clickByFirstTxt(root: AccessibilityNodeInfo?, txt: String, match: EMCMatch = EMCMatch.CLICKABLE): Boolean =
        getByFirstTxt(root, txt)?.let {
            click(it, match)
        } ?: false

    /**
     * 选中节点
     */
    fun select(
        root: AccessibilityNodeInfo?,
        match: EMCMatch = EMCMatch.CHECKABLE
    ): Boolean {
        if (root == null) return false
        if (isAttributesMatch(root, match)) return root.performAction(AccessibilityNodeInfo.ACTION_SELECT)

        val node = getFirstAttrMatchNode(root, match)
        return node?.performAction(AccessibilityNodeInfo.ACTION_SELECT) ?: false
    }

    /**
     * 点击节点 - 首个匹配 txt 节点
     */
    fun selectByFirstTxt(
        root: AccessibilityNodeInfo?, txt: String,
        match: EMCMatch = EMCMatch.CHECKABLE
    ): Boolean = getByFirstTxt(root, txt)?.let {
        select(it, match)
    } ?: false

    /**
     * 点击节点 - 首个匹配 id 节点
     */
    fun selectByFirstId(
        root: AccessibilityNodeInfo?, id: String,
        match: EMCMatch = EMCMatch.CHECKABLE
    ): Boolean = getByFirstId(root, id)?.let {
        select(it, match)
    } ?: false

    fun log(nodeInfo: AccessibilityNodeInfo?, filterGone: Boolean = false) {
        nodeInfo ?: return

        if (filterGone && !nodeInfo.isVisibleToUser) return

        val data = mutableMapOf<String, Any?>()

        data["windowId"] = nodeInfo.windowId
        data["window"] = nodeInfo.window
        data["packageName"] = nodeInfo.packageName
        data["className"] = nodeInfo.className
        data["inputType"] = nodeInfo.inputType

        data["viewId"] = nodeInfo.viewIdResourceName
        data["text"] = nodeInfo.text
        data["isChecked"] = nodeInfo.isChecked
        data["isEnabled"] = nodeInfo.isEnabled
        data["isFocused"] = nodeInfo.isFocused
        data["isSelected"] = nodeInfo.isSelected
        data["isVisibleToUser"] = nodeInfo.isVisibleToUser

        data["isClickable"] = nodeInfo.isClickable
        data["isCheckable"] = nodeInfo.isCheckable
        data["isEditable"] = nodeInfo.isEditable
        data["isFocusable"] = nodeInfo.isFocusable
        data["isScrollable"] = nodeInfo.isScrollable
        data["isDismissable"] = nodeInfo.isDismissable
        data["isContentInvalid"] = nodeInfo.isContentInvalid
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            data["isContextClickable"] = nodeInfo.isContextClickable
        }

        data["childCount"] = nodeInfo.childCount
        data["actionList"] = nodeInfo.actionList

        data["detail"] = nodeInfo.toString()

        XLog.tag("Node-Log").json(JSONUtil.toJsonStr(data, JSONConfig.create().setOrder(true)))
    }

    fun logRoot(root: AccessibilityNodeInfo?) {
        root ?: return

        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)

        while (!queue.isEmpty()) {
            val node = queue.removeFirst()
            log(node, true)

            for (index in 0 until node.childCount) {
                queue.addLast(node.getChild(index))
            }
        }
    }

}