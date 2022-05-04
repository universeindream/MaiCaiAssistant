package com.univerindream.maicaiassistant.utils

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.os.Build
import android.view.accessibility.AccessibilityNodeInfo
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.univerindream.maicaiassistant.model.ECond
import com.univerindream.maicaiassistant.model.EHandle
import com.univerindream.maicaiassistant.model.MCHandle
import com.univerindream.maicaiassistant.model.MCNode
import com.univerindream.maicaiassistant.ui.MainActivity
import kotlinx.coroutines.delay

object NodeInfoUtils {

    /**
     * 校验步骤条件
     */
    fun stepCond(
        rootInActiveWindow: AccessibilityNodeInfo?,
        packageNameByWindowId: Map<Int, String>,
        activityNameByWindowId: Map<Int, String>,
        cond: ECond,
        condNode: MCNode
    ): Boolean {
        return when (cond) {
            ECond.APP_IS_BACKGROUND -> rootInActiveWindow?.packageName != condNode.packageName
            ECond.APP_IS_EQUAL -> packageNameByWindowId[rootInActiveWindow?.windowId] == condNode.packageName
            ECond.PAGE_IS_EQUAL -> activityNameByWindowId[rootInActiveWindow?.windowId] == condNode.className
            ECond.NODE_IS_MATCH -> isMatch(rootInActiveWindow, condNode)
            ECond.NODE_NO_MATCH -> !isMatch(rootInActiveWindow, condNode)
            else -> false
        }
    }

    /**
     * 执行步骤操作
     */
    suspend fun stepHandle(
        service: AccessibilityService,
        rootInActiveWindow: AccessibilityNodeInfo?,
        handle: MCHandle
    ): Boolean {
        val type = handle.type
        val node = handle.node
        val delayRunBefore = handle.delayRunBefore
        val delayRunAfter = handle.delayRunAfter

        if (delayRunBefore > 0) delay(delayRunBefore)

        val result: Boolean = when (type) {
            EHandle.LAUNCH -> {
                ActivityUtils.startActivity(MainActivity::class.java)
                delay(500)
                AppUtils.launchApp(node.packageName)
                delay(500)
                true
            }
            EHandle.BACK -> {
                service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
            }
            EHandle.RECENTS -> {
                service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS)
            }
            EHandle.CLICK_NODE -> {
                val matchNodes = getNodes(rootInActiveWindow, node)
                val nodeInfo = if (node.index >= 0) matchNodes.getOrNull(node.index) else matchNodes.randomOrNull()
                nodeInfo ?: return false

                clickNode(nodeInfo)
            }
            EHandle.CLICK_SCREEN -> {
                val x = node.coordinate.split(",").getOrNull(0)?.toFloatOrNull()?.toInt() ?: return false
                val y = node.coordinate.split(",").getOrNull(1)?.toFloatOrNull()?.toInt() ?: return false
                clickScreen(service, Rect(x, y, x, y))
            }
            EHandle.NONE -> true
        }

        if (delayRunAfter > 0) delay(delayRunAfter)

        return result
    }

    /**
     * 是否匹配
     */
    fun isMatch(root: AccessibilityNodeInfo?, node: MCNode): Boolean {
        val matchNodes = getNodes(root, node)
        return when {
            node.index >= 0 -> isAttrMatch(matchNodes.getOrNull(node.index), node)
            else -> matchNodes.all { isAttrMatch(it, node) }
        }
    }

    /**
     * 属性是否匹配
     */
    fun isAttrMatch(nodeInfo: AccessibilityNodeInfo?, node: MCNode): Boolean {
        nodeInfo ?: return false

        if (node.isEnabled != null && node.isEnabled != nodeInfo.isEnabled) return false
        if (node.isFocused != null && node.isFocused != nodeInfo.isFocused) return false
        if (node.isChecked != null && node.isChecked != nodeInfo.isChecked) return false
        if (node.isSelected != null && node.isSelected != nodeInfo.isSelected) return false
        if (node.isVisibleToUser != null && node.isVisibleToUser != nodeInfo.isVisibleToUser) return false

        if (node.isClickable != null && node.isClickable != nodeInfo.isClickable) return false
        if (node.isCheckable != null && node.isCheckable != nodeInfo.isCheckable) return false
        if (node.isEditable != null && node.isEditable != nodeInfo.isEditable) return false
        if (node.isScrollable != null && node.isScrollable != nodeInfo.isScrollable) return false

        return true
    }

    /**
     * 获取节点，支持三种方式（id,txt,坐标）
     */
    fun getNodes(root: AccessibilityNodeInfo?, node: MCNode): List<AccessibilityNodeInfo> {
        root ?: return emptyList()

        val result = arrayListOf<AccessibilityNodeInfo>()

        if (node.id.isNotBlank()) {
            result.addAll(root.findAccessibilityNodeInfosByViewId(node.id))
        } else if (node.txt.isNotBlank()) {
            result.addAll(root.findAccessibilityNodeInfosByText(node.txt))
        } else if (node.coordinate.isNotBlank()) {
            val x = node.coordinate.split(",").getOrNull(0)?.toFloatOrNull()?.toInt()
            val y = node.coordinate.split(",").getOrNull(1)?.toFloatOrNull()?.toInt()
            if (x != null && y != null) {
                val queue = ArrayDeque<AccessibilityNodeInfo>()
                queue.add(root)

                while (!queue.isEmpty()) {
                    val rect = Rect()
                    val temp = queue.removeFirst()
                    temp.getBoundsInScreen(rect)
                    if (x in rect.left..rect.right && y in rect.top..rect.bottom) {
                        result.add(temp)

                        repeat(temp.childCount) {
                            queue.addLast(temp.getChild(it))
                        }
                    }
                }
            }
        }

        return result
    }

    /**
     * 点击屏幕
     */
    fun clickScreen(service: AccessibilityService, rect: Rect): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
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
        } else false
    }

    /**
     * 点击控件 - 事件点击
     */
    fun clickNode(node: AccessibilityNodeInfo?): Boolean {
        node ?: return false

        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(node)

        while (!queue.isEmpty()) {
            val temp = queue.removeLast()

            if (temp.isClickable) {
                return temp.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }

            temp.parent?.let {
                queue.addFirst(it)
            }
        }

        return false
    }

}