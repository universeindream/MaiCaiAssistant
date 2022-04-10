package com.univerindream.maicaiassistant.utils

import android.os.Build
import android.view.accessibility.AccessibilityNodeInfo
import com.elvishew.xlog.XLog
import com.google.gson.Gson
import com.univerindream.maicaiassistant.EMCMatch
import com.univerindream.maicaiassistant.EMCSearch
import com.univerindream.maicaiassistant.MCNode

object NodeUtil {

    /**
     * 属性是否匹配
     */
    fun isMatch(info: AccessibilityNodeInfo?, match: EMCMatch = EMCMatch.ALL): Boolean {
        if (info == null) return false

        return when (match) {
            EMCMatch.CLICKABLE -> info.isClickable && info.isEnabled
            EMCMatch.CLICKABLE_SELF_OR_PARENT -> {
                val queue = ArrayDeque<AccessibilityNodeInfo>()
                queue.add(info)

                while (!queue.isEmpty()) {
                    val node = queue.removeLast()

                    if (node.isClickable && node.isEnabled) {
                        return true
                    }

                    node.parent?.let {
                        queue.addFirst(it)
                    }

                }

                return false
            }
            EMCMatch.CLICKABLE_WITH_DISABLE -> info.isClickable

            EMCMatch.CHECKABLE -> info.isCheckable && info.isEnabled
            EMCMatch.CHECKABLE_SELF_OR_BROTHER -> {
                if (info.isCheckable && info.isEnabled) return true

                val parent = info.parent
                val child = arrayListOf<AccessibilityNodeInfo>()

                for (index in 0 until parent.childCount) {
                    child.add(parent.getChild(index))
                }

                return child.any { it.isCheckable && it.isEnabled }
            }
            EMCMatch.CHECKABLE_WITH_DISABLE -> info.isCheckable

            EMCMatch.SELECTED -> info.isSelected && info.isEnabled
            EMCMatch.SELECTED_SELF_OR_BROTHER -> {
                if (info.isSelected && info.isEnabled) return true

                val parent = info.parent
                val child = arrayListOf<AccessibilityNodeInfo>()

                for (index in 0 until parent.childCount) {
                    child.add(parent.getChild(index))
                }

                return child.any { it.isSelected && it.isEnabled }
            }
            EMCMatch.SELECTED_WITH_DISABLE -> info.isSelected

            EMCMatch.CHECKED -> info.isChecked && info.isEnabled
            EMCMatch.CHECKED_SELF_OR_BROTHER -> {
                if (info.isChecked && info.isEnabled) return true

                val parent = info.parent
                val child = arrayListOf<AccessibilityNodeInfo>()

                for (index in 0 until parent.childCount) {
                    child.add(parent.getChild(index))
                }

                return child.any { it.isChecked && it.isEnabled }
            }
            EMCMatch.CHECKED_WITH_DISABLE -> info.isChecked

            EMCMatch.ALL -> true
        }
    }

    fun searchAllNode(
        root: AccessibilityNodeInfo?,
        node: MCNode,
        match: EMCMatch = EMCMatch.ALL
    ): List<AccessibilityNodeInfo> {
        root ?: return emptyList()

        val result = arrayListOf<AccessibilityNodeInfo>()

        val nodeType = node.search
        val nodeKey = node.nodeKey
        val className = node.className
        val packageName = node.packageName

        if (nodeType == EMCSearch.PACKAGE_NAME) {
            if (root.packageName == packageName) {
                result.add(root)
            }
            return result
        }

        if (nodeType == EMCSearch.CLASSNAME) {
            if (root.className == className) {
                result.add(root)
            }
            return result
        }

        var data = when (nodeType) {
            EMCSearch.ID -> root.findAccessibilityNodeInfosByViewId(nodeKey)
            EMCSearch.TXT -> root.findAccessibilityNodeInfosByText(nodeKey)
            else -> arrayListOf<AccessibilityNodeInfo>()
        }

        if (className.isNotBlank()) {
            data = data.filter { it.className == className }
        }

        if (match != EMCMatch.ALL) {
            data = data.filter { isMatch(it, match) }
        }

        return data
    }

    fun searchFirstNode(
        root: AccessibilityNodeInfo?,
        node: MCNode,
        match: EMCMatch = EMCMatch.ALL
    ): AccessibilityNodeInfo? =
        searchAllNode(root, node, match).firstOrNull()

    fun isExist(root: AccessibilityNodeInfo?, node: MCNode, match: EMCMatch = EMCMatch.ALL): Boolean =
        searchAllNode(root, node, match).isNotEmpty()

    fun click(root: AccessibilityNodeInfo?): Boolean {
        root ?: return false

        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)

        while (!queue.isEmpty()) {
            val node = queue.removeLast()

            if (node.isClickable && node.isEnabled) {
                return node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }

            node.parent?.let {
                queue.addFirst(it)
            }

        }

        return false
    }

    fun clickFirstNode(root: AccessibilityNodeInfo?, node: MCNode, match: EMCMatch = EMCMatch.ALL): Boolean {
        return click(searchFirstNode(root, node, match))
    }

    fun clickRandomNode(root: AccessibilityNodeInfo?, node: MCNode, match: EMCMatch = EMCMatch.ALL): Boolean {
        return click(searchAllNode(root, node, match).randomOrNull())
    }

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

        XLog.tag("Node-Log").json(Gson().toJson(data))
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