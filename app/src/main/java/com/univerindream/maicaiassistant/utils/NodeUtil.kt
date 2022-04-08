package com.univerindream.maicaiassistant.utils

import android.os.Build
import android.view.accessibility.AccessibilityNodeInfo
import cn.hutool.json.JSONConfig
import cn.hutool.json.JSONUtil
import com.elvishew.xlog.XLog

object NodeUtil {
    /**
     * 获取节点 - 首个匹配 txt 节点
     */
    fun getByTxtAndFirstMatch(root: AccessibilityNodeInfo?, txt: String) =
        root?.findAccessibilityNodeInfosByText(txt)?.getOrNull(0)

    /**
     * 获取节点 - 首个匹配 id 节点
     */
    fun getByIdAndFirstMatch(root: AccessibilityNodeInfo?, id: String) =
        root?.findAccessibilityNodeInfosByViewId(id)?.getOrNull(0)

    /**
     * 获取点击节点 - 指定节点下及父节树
     */
    fun getListByClick(root: AccessibilityNodeInfo?): ArrayList<AccessibilityNodeInfo> {
        if (root == null) return arrayListOf()

        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)

        val data = ArrayList<AccessibilityNodeInfo>()

        while (!queue.isEmpty()) {
            val node = queue.removeFirst()

            if (node.isClickable && node.isEnabled) {
                data.add(node)
            }

            node.parent?.let {
                queue.addFirst(it)
            }
        }

        return data
    }

    /**
     * 获取点击节点 - 第一个匹配的
     */
    fun getByFirstMatchClick(root: AccessibilityNodeInfo?): AccessibilityNodeInfo? {
        if (root == null) return null
        if (isClick(root)) return root

        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)

        while (!queue.isEmpty()) {
            val node = queue.removeFirst()

            if (node.isClickable && node.isEnabled) {
                return node
            }

            node.parent?.let {
                queue.addFirst(it)
            }
        }

        return null
    }

    /**
     * 节点是否可以点击
     */
    fun isClick(root: AccessibilityNodeInfo?) = root != null && root.isChecked && root.isClickable

    /**
     * 是否可以点击 - 指定节点
     */
    fun isClickAndIncludeParent(root: AccessibilityNodeInfo?): Boolean {
        return isClick(root) || getListByClick(root).size > 0
    }

    /**
     * 是否可以点击 - 任意一个匹配 txt 节点
     */
    fun isClickByTxt(root: AccessibilityNodeInfo?, txt: String): Boolean {
        if (root == null) return false

        val data = root.findAccessibilityNodeInfosByText(txt)
        data.forEach {
            if (isClickAndIncludeParent(it)) return true
        }

        return false
    }

    /**
     * 是否可以点击 - 首个匹配 txt 节点
     */
    fun isClickByFirstMatchTxt(root: AccessibilityNodeInfo?, txt: String): Boolean {
        if (root == null) return false
        val node = getByTxtAndFirstMatch(root, txt)
        return node != null && isClickAndIncludeParent(node)
    }

    /**
     * 点击节点
     */
    fun click(
        root: AccessibilityNodeInfo?
    ): Boolean {
        if (root == null) return false
        if (isClick(root)) return root.performAction(AccessibilityNodeInfo.ACTION_CLICK)

        val node = getByFirstMatchClick(root)
        return node?.performAction(AccessibilityNodeInfo.ACTION_CLICK) ?: false
    }

    /**
     * 点击节点 - 首个匹配 txt 节点
     */
    fun clickByFirstMatchTxt(root: AccessibilityNodeInfo?, txt: String): Boolean {
        return getByTxtAndFirstMatch(root, txt)?.let {
            click(it)
        } ?: false
    }

    /**
     * 点击节点 - 首个匹配 id 节点
     */
    fun clickByFirstMatchId(root: AccessibilityNodeInfo?, id: String): Boolean {
        return getByIdAndFirstMatch(root, id)?.let {
            click(it)
        } ?: false
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