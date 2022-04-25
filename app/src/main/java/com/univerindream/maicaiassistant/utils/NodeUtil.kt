package com.univerindream.maicaiassistant.utils

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.os.Build
import android.view.accessibility.AccessibilityNodeInfo
import com.elvishew.xlog.XLog
import com.google.gson.Gson
import com.univerindream.maicaiassistant.EMCMatch
import com.univerindream.maicaiassistant.EMCNodeType
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
            EMCMatch.VISIBLE -> info.isVisibleToUser

            EMCMatch.ALL -> true
        }
    }

    fun searchNodeByXY(root: AccessibilityNodeInfo?, x: Int, y: Int): List<AccessibilityNodeInfo> {
        root ?: return emptyList()

        val data = arrayListOf<AccessibilityNodeInfo>()

        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)

        while (!queue.isEmpty()) {
            val node = queue.removeFirst()

            val rect = Rect()
            node.getBoundsInScreen(rect)
            if (x in rect.left..rect.right && y in rect.top..rect.bottom) {
                data.add(node)

                repeat(node.childCount) {
                    queue.addLast(node.getChild(it))
                }
            }
        }

        return data
    }

    fun searchAllNode(
        root: AccessibilityNodeInfo?,
        node: MCNode
    ): List<AccessibilityNodeInfo> {
        root ?: return emptyList()

        val nodeType = node.nodeType
        val nodeKey = node.nodeKey
        val className = node.className
        val packageName = node.packageName

        var data = when (nodeType) {
            EMCNodeType.ID -> root.findAccessibilityNodeInfosByViewId(nodeKey)
            EMCNodeType.TXT -> root.findAccessibilityNodeInfosByText(nodeKey)
            EMCNodeType.PACKAGE_NAME -> return if (root.packageName == packageName) arrayListOf(root) else arrayListOf()
            EMCNodeType.CLASSNAME -> return if (root.className == className) arrayListOf(root) else arrayListOf()
        }

        if (className.isNotBlank()) {
            data = data.filter { it.className == className }
        }

        return data
    }

    fun isExist(root: AccessibilityNodeInfo?, node: MCNode, match: EMCMatch = EMCMatch.ALL): Boolean {
        val nodeInfo = searchAllNode(root, node).getOrNull(node.nodeIndex)
        return isMatch(nodeInfo, match)
    }

    fun click(root: AccessibilityNodeInfo?, justSelf: Boolean = false): Boolean {
        root ?: return false

        if (justSelf) return root.performAction(AccessibilityNodeInfo.ACTION_CLICK)

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

    fun clickByGesture(service: AccessibilityService, nodeInfo: AccessibilityNodeInfo?): Boolean {
        nodeInfo ?: return false
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val rect = Rect()
            nodeInfo.getBoundsInScreen(rect)
            val x = rect.exactCenterX()
            val y = rect.exactCenterY()

            val builder = GestureDescription.Builder()
            val path = Path()
            path.moveTo(x, y)
            path.lineTo(x, y)
            builder.addStroke(GestureDescription.StrokeDescription(path, 1, 1))
            service.dispatchGesture(builder.build(), object : AccessibilityService.GestureResultCallback() {
                override fun onCancelled(gestureDescription: GestureDescription) {
                    super.onCancelled(gestureDescription)
                }

                override fun onCompleted(gestureDescription: GestureDescription) {
                    super.onCompleted(gestureDescription)
                }
            }, null)
        } else {
            click(nodeInfo)
        }
    }

    fun clickNode(root: AccessibilityNodeInfo?, node: MCNode, isSelf: Boolean = false): Boolean {
        val nodeInfo = searchAllNode(root, node).getOrNull(node.nodeIndex) ?: return false
        return click(nodeInfo, isSelf)
    }

    fun clickRandomNode(root: AccessibilityNodeInfo?, node: MCNode, isSelf: Boolean = false): Boolean {
        val nodeInfo = searchAllNode(root, node).randomOrNull()
        return click(nodeInfo, isSelf)
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