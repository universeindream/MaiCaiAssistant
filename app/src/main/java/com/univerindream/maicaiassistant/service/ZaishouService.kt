package com.univerindream.maicaiassistant.service

import android.accessibilityservice.AccessibilityService
import android.graphics.PixelFormat
import android.graphics.Rect
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.*
import com.jeremyliao.liveeventbus.LiveEventBus
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.univerindream.maicaiassistant.LEBConstants
import com.univerindream.maicaiassistant.MCUtil
import com.univerindream.maicaiassistant.R
import com.univerindream.maicaiassistant.adapter.BindingSearchNodeItem
import com.univerindream.maicaiassistant.data.DataRepository
import com.univerindream.maicaiassistant.databinding.WindowCheckerBinding
import com.univerindream.maicaiassistant.databinding.WindowDashboardBinding
import com.univerindream.maicaiassistant.databinding.WindowMessageBinding
import com.univerindream.maicaiassistant.model.MCNode
import com.univerindream.maicaiassistant.model.MCNodeMessage
import com.univerindream.maicaiassistant.model.MCStepLog
import com.univerindream.maicaiassistant.utils.NodeInfoUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject


@AndroidEntryPoint
class ZaishouService : AccessibilityService() {

    private val mServiceScope = CoroutineScope(Dispatchers.Main + Job())
    private val mTaskScope = CoroutineScope(Dispatchers.Main + Job())
    private var mTaskJob: Job? = null

    @Inject
    lateinit var dataRepository: DataRepository

    private val mWindowManager by lazy {
        getSystemService(WINDOW_SERVICE) as WindowManager
    }

    private lateinit var mWinDashBoardBinding: WindowDashboardBinding
    private val mWinDashBoardLayoutParams: WindowManager.LayoutParams by lazy {
        WindowManager.LayoutParams().also {
            it.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
            it.format = PixelFormat.TRANSLUCENT
            it.flags =
                it.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            it.width = WindowManager.LayoutParams.WRAP_CONTENT
            it.height = WindowManager.LayoutParams.WRAP_CONTENT
            it.gravity = Gravity.START or Gravity.TOP
            it.x = ScreenUtils.getScreenWidth() - ConvertUtils.dp2px(40f)
            it.y = ScreenUtils.getScreenHeight() / 2 - ConvertUtils.dp2px(88f)
        }
    }

    private lateinit var mWinMessageBinding: WindowMessageBinding
    private val mWinMessageLayoutParams: WindowManager.LayoutParams by lazy {
        WindowManager.LayoutParams().also {
            it.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
            it.format = PixelFormat.TRANSLUCENT
            it.flags =
                it.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            it.width = WindowManager.LayoutParams.WRAP_CONTENT
            it.height = WindowManager.LayoutParams.WRAP_CONTENT
            it.gravity = Gravity.START or Gravity.TOP
            it.x = ConvertUtils.dp2px(20f)
            it.y = ConvertUtils.dp2px(20f)
        }
    }

    private lateinit var mWinCheckerBinding: WindowCheckerBinding
    private val mWinCheckerLayoutParams: WindowManager.LayoutParams by lazy {
        WindowManager.LayoutParams().also {
            it.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
            it.format = PixelFormat.TRANSLUCENT
            it.flags =
                it.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            it.width = WindowManager.LayoutParams.WRAP_CONTENT
            it.height = WindowManager.LayoutParams.WRAP_CONTENT
            it.gravity = Gravity.TOP or Gravity.START
            it.x = ScreenUtils.getAppScreenWidth() / 2
            it.y = ScreenUtils.getAppScreenHeight() / 2
        }
    }

    private val itemAdapter by lazy {
        ItemAdapter<BindingSearchNodeItem>()
    }

    private val fastAdapter by lazy {
        FastAdapter.with(itemAdapter)
    }

    private val mWinMessageList = ArrayDeque<String>()

    private var mTaskJobLaunchTime = AtomicLong(System.currentTimeMillis())
    private var mTaskJobLaunchLog = LinkedHashMap<Int, MCStepLog>()

    private var mCurPackageName: String = "-"
    private var mCurActivityName: String = "-"
    private var mPackageNameByWindowId = mutableMapOf<Int, String>()
    private var mActivityNameByWindowId = mutableMapOf<Int, String>()
    private val mCurPackageNameByRootWindow: String
        get() {
            val pn = mPackageNameByWindowId[rootInActiveWindow?.windowId]
            return if (pn.isNullOrBlank()) {
                mCurPackageName
            } else {
                mCurPackageName = pn
                pn
            }
        }
    private val mCurActivityNameByRootWindow: String
        get() {
            val cn = mActivityNameByWindowId[rootInActiveWindow?.windowId]
            return if (cn.isNullOrBlank()) {
                mCurActivityName
            } else {
                mCurActivityName = cn
                cn
            }
        }
    private val mLaunchSolutionObserver: Observer<String> by lazy {
        Observer<String> { launchSolution() }
    }

    override fun onCreate() {
        super.onCreate()

        MCUtil.startForegroundService(this)

        LiveEventBus.get<String>(LEBConstants.LAUNCH_SOLUTION).observeForever(mLaunchSolutionObserver)
    }

    override fun onServiceConnected() {
        LogUtils.v("onServiceConnected")

        initWinDashboard()
        initWinMessage()
        initWinChecker()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val pName = event.packageName?.toString() ?: ""
        val cName = event.className?.toString() ?: ""
        val windowId = event.windowId
        val source = event.source

        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                LogUtils.v("TYPE_WINDOW_CONTENT_CHANGED - %s - %s", event, source)
            }
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                LogUtils.v("TYPE_WINDOW_STATE_CHANGED - %s - %s", event, source)
                mServiceScope.launch {
                    if (ActivityUtils.isActivityExists(pName, cName)) {
                        mCurPackageName = pName
                        mCurActivityName = cName

                        mPackageNameByWindowId[windowId] = pName
                        mActivityNameByWindowId[windowId] = cName
                    }
                }
            }
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                LogUtils.v("TYPE_VIEW_CLICKED - %s - %s", event, source)
            }
            else -> {}
        }
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        LiveEventBus.get<String>(LEBConstants.LAUNCH_SOLUTION).removeObserver(mLaunchSolutionObserver)
    }

    private fun initWinDashboard() {
        mWinDashBoardBinding = WindowDashboardBinding.inflate(LayoutInflater.from(this))
        mWindowManager.addView(mWinDashBoardBinding.root, mWinDashBoardLayoutParams)

        mWinDashBoardBinding.btnShowSearch.setOnClickListener {
            setWinMessageView(!mWinCheckerBinding.root.isVisible, true)
        }
        mWinDashBoardBinding.btnConfig.setOnClickListener {
            AppUtils.launchApp(AppUtils.getAppPackageName())
        }
        mWinDashBoardBinding.btnAlarmOff.setOnClickListener {
            MCUtil.stopRingTone()
        }
        mWinDashBoardBinding.btnSnapUp.setOnClickListener {
            if (it.tag == true) {
                cancelSolution()
            } else {
                launchSolution()
            }
        }

        var x = 0
        var y = 0
        mWinDashBoardBinding.btnMove.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = event.rawX.toInt()
                    y = event.rawY.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    val nowX = event.rawX.toInt()
                    val nowY = event.rawY.toInt()
                    val movedX = nowX - x
                    val movedY = nowY - y
                    x = nowX
                    y = nowY

                    mWinDashBoardLayoutParams.x = mWinDashBoardLayoutParams.x + movedX
                    mWinDashBoardLayoutParams.y = mWinDashBoardLayoutParams.y + movedY
                    mWindowManager.updateViewLayout(mWinDashBoardBinding.root, mWinDashBoardLayoutParams);
                }
            }
            return@setOnTouchListener true
        }
    }

    private fun initWinMessage() {
        mWinMessageBinding = WindowMessageBinding.inflate(LayoutInflater.from(this))
        mWinMessageBinding.root.visibility = View.GONE
        mWindowManager.addView(mWinMessageBinding.root, mWinMessageLayoutParams)

        mWinMessageBinding.ivClose.setOnClickListener {
            setWinMessageView(false)
        }

        var x = 0
        var y = 0
        mWinMessageBinding.ivMove.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = event.rawX.toInt()
                    y = event.rawY.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    val nowX = event.rawX.toInt()
                    val nowY = event.rawY.toInt()
                    val movedX = nowX - x
                    val movedY = nowY - y
                    x = nowX
                    y = nowY

                    mWinMessageLayoutParams.x = mWinMessageLayoutParams.x + movedX
                    mWinMessageLayoutParams.y = mWinMessageLayoutParams.y + movedY
                    mWindowManager.updateViewLayout(mWinMessageBinding.root, mWinMessageLayoutParams);
                }
            }
            return@setOnTouchListener true
        }

        mWinMessageBinding.rvInfo.adapter = fastAdapter
        mWinMessageBinding.rvInfo.layoutManager = LinearLayoutManager(this)
        fastAdapter.onClickListener = { _, _, item, _ ->
            if (item.model.node != null) {
                updateRVMessage(item.model.node)
            } else {
                ClipboardUtils.copyText(item.model.value.toString())
                ToastUtils.showShort("${item.model.name} 已复制")
            }
            true
        }
    }

    private fun initWinChecker() {
        mWinCheckerBinding = WindowCheckerBinding.inflate(LayoutInflater.from(this))
        mWinCheckerBinding.root.visibility = View.GONE
        mWindowManager.addView(mWinCheckerBinding.root, mWinCheckerLayoutParams)

        val statusBarHeight = BarUtils.getStatusBarHeight()
        mWinCheckerBinding.ivSearchMove.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {}
                MotionEvent.ACTION_MOVE -> {
                    val nowX = event.rawX.toInt()
                    val nowY = event.rawY.toInt()

                    mWinCheckerLayoutParams.x = nowX - mWinCheckerBinding.root.width / 2
                    mWinCheckerLayoutParams.y = nowY - mWinCheckerBinding.root.height / 2 - statusBarHeight
                    mWindowManager.updateViewLayout(mWinCheckerBinding.root, mWinCheckerLayoutParams);
                }
                MotionEvent.ACTION_UP -> {
                    val x = event.rawX.toInt()
                    val y = event.rawY.toInt()

                    NodeInfoUtils.getNodes(rootInActiveWindow, MCNode(coordinate = "$x,$y")).lastOrNull()?.let {
                        updateRVMessage(it, "$x,$y")
                    }
                }
            }
            return@setOnTouchListener true
        }
    }

    private fun launchSolution() {
        mTaskJobLaunchTime.set(System.currentTimeMillis())
        mTaskJobLaunchLog.clear()

        mServiceScope.launch(Dispatchers.Main) {
            mWinDashBoardBinding.btnSnapUp.tag = true
            mWinDashBoardBinding.btnSnapUp.setImageResource(R.drawable.baseline_stop_24)
            setWinMessageView(true)
        }

        mTaskJob?.cancel()
        mTaskJob = null
        mTaskJob = mTaskScope.launch(Dispatchers.IO) {
            val solution = dataRepository.curSolution
            loop@ while (isActive) {
                //任务时长是否达标
                if (System.currentTimeMillis() > mTaskJobLaunchTime.get() + solution.launchTime * 1000 * 60) {
                    val message = "已执行了 ${solution.launchTime} 分钟"
                    LogUtils.i("loop - %s", message)
                    MCUtil.notify("完成提示", message)
                    cancelSolution()
                    break@loop
                }

                //循环执行步骤
                val steps = solution.steps
                for ((index, step) in steps.withIndex()) {
                    //步骤未启用
                    if (!step.isEnable) continue

                    //步骤只执行一次
                    if (step.isExecuteOnce && mTaskJobLaunchLog.containsKey(index)) continue

                    //步骤不匹配
                    val isMatch = step.condList.all {
                        NodeInfoUtils.stepCond(
                            rootInActiveWindow,
                            mPackageNameByWindowId,
                            mActivityNameByWindowId,
                            it.type,
                            it.node
                        )
                    }
                    if (!isMatch) continue

                    val stepName = "步骤${index + 1}.${step.name}"
                    LogUtils.v("stepName - $stepName")

                    //步骤日志
                    val stepLog = "${TimeUtils.millis2String(System.currentTimeMillis(), "mm:ss.SSS")} $stepName"
                    updateTextMessage(stepLog)

                    //步骤记录
                    val prevLogStep = mTaskJobLaunchLog.values.lastOrNull()
                    if (prevLogStep == null || prevLogStep.index != index || step.isRepeat) {
                        mTaskJobLaunchLog.remove(index)
                        mTaskJobLaunchLog[index] = MCStepLog(index, step, System.currentTimeMillis())
                    }

                    //步骤警报
                    if (step.isAlarm) {
                        MCUtil.notify("响铃提示", "$stepName，触发响铃")
                        MCUtil.playRingTone()
                    }

                    //步骤人工
                    if (step.isManual) {
                        MCUtil.notify("人工提示", "$stepName，人工操作")
                        cancelSolution()
                        break@loop
                    }

                    val handleResult =
                        NodeInfoUtils.stepHandle(this@ZaishouService, rootInActiveWindow, step.handle)
                    LogUtils.v("steps - $stepName - 执行结果 - $handleResult")

                    //步骤失败返回
                    if (step.isFailBack && !handleResult) {
                        repeat(step.failBackCount) {
                            performGlobalAction(GLOBAL_ACTION_BACK)
                            delay(200)
                        }
                        continue
                    }

                    //步骤不可重复且最后一个
                    if (!step.isRepeat && step == steps.last()) {
                        MCUtil.notify("终止提示", "$stepName 为最后步骤，终止任务")
                        cancelSolution()
                        break@loop
                    }

                    //步骤达标, 终止本次流程
                    break
                }

                //10s 异常捕捉
                val lastStep = mTaskJobLaunchLog.values.lastOrNull()
                if (lastStep != null) {
                    val executeTime = lastStep.executionTime
                    if (executeTime + solution.failDuration < System.currentTimeMillis()) {
                        val stepName = "步骤${lastStep.index + 1}.${lastStep.step.name}"
                        val message = "\"$stepName\" 执行失败!!!"
                        LogUtils.e("$stepName - %s", message)
                        MCUtil.notify("失败提示", message)
                        ToastUtils.showLong(message)
                        if (solution.failIsAlarm) MCUtil.playRingTone()
                        cancelSolution()
                        break@loop
                    }
                }

                delay(solution.stepDelay)
            }
        }
    }

    private fun cancelSolution() {
        mServiceScope.launch(Dispatchers.Main) {
            mWinDashBoardBinding.btnSnapUp.tag = false
            mWinDashBoardBinding.btnSnapUp.setImageResource(R.drawable.baseline_play_arrow_24)
            setWinMessageView(false)
        }

        mTaskJob?.cancel()
        mTaskJob = null
    }

    private fun setWinMessageView(visible: Boolean = false, hasSearch: Boolean = false) {
        mWinMessageBinding.tvInfo.text = ""
        mWinMessageList.clear()
        itemAdapter.clear()

        mWinCheckerBinding.root.visibility = View.GONE
        mWinMessageBinding.root.visibility = View.GONE
        mWinMessageBinding.rvInfo.visibility = View.GONE
        mWinMessageBinding.tvInfo.visibility = View.GONE

        if (visible) {
            mWinMessageBinding.root.visibility = View.VISIBLE

            if (hasSearch) {
                mWinCheckerLayoutParams.x = ScreenUtils.getAppScreenWidth() / 2
                mWinCheckerLayoutParams.y = ScreenUtils.getAppScreenHeight() / 2
                mWindowManager.updateViewLayout(mWinCheckerBinding.root, mWinCheckerLayoutParams);

                mWinCheckerBinding.root.visibility = View.VISIBLE
                mWinMessageBinding.rvInfo.visibility = View.VISIBLE
            } else {
                mWinMessageBinding.tvInfo.visibility = View.VISIBLE
            }
        }
    }

    private suspend fun updateTextMessage(info: String) {
        if (mWinMessageBinding.root.isGone) return

        if (mWinMessageList.size >= 6) mWinMessageList.removeLast()
        mWinMessageList.addFirst(info)
        withContext(Dispatchers.Main) {
            mWinMessageBinding.tvInfo.text = mWinMessageList.joinToString("\n")
        }
    }

    private fun updateRVMessage(node: AccessibilityNodeInfo?, coordinate: String = "") {
        itemAdapter.clear()
        if (coordinate.isNotBlank()) itemAdapter.add(BindingSearchNodeItem(MCNodeMessage("坐标(x,y)", coordinate)))
        val appInfo = AppUtils.getAppInfo(mCurPackageNameByRootWindow)
        itemAdapter.add(
            BindingSearchNodeItem(MCNodeMessage("------- 通用属性 -------", "")),
            BindingSearchNodeItem(MCNodeMessage("软件包名(${appInfo?.name})", mCurPackageNameByRootWindow)),
            BindingSearchNodeItem(MCNodeMessage("页面类名", mCurActivityNameByRootWindow)),
        )

        var idIndex = "-"
        if (!node?.viewIdResourceName.isNullOrBlank()) {
            val allSearch = NodeInfoUtils.getNodes(rootInActiveWindow, MCNode(id = node?.viewIdResourceName.toString()))
            val index = allSearch.indexOf(node)
            idIndex = if (index >= 0) "${index + 1}" else "-"
        }

        var txtIndex = "-"
        if (!node?.text.isNullOrBlank()) {
            val allSearch = NodeInfoUtils.getNodes(rootInActiveWindow, MCNode(txt = node?.text.toString()))
            val index = allSearch.indexOf(node)
            txtIndex = if (index >= 0) "${index + 1}" else "-"
        }
        var temp: AccessibilityNodeInfo? = node
        var depth = 0
        while (temp != null) {
            temp = temp.parent
            depth++
        }

        val rect = Rect()
        node?.getBoundsInScreen(rect)

        itemAdapter.add(
            BindingSearchNodeItem(MCNodeMessage("------- 控件属性 -------", "")),
            BindingSearchNodeItem(MCNodeMessage(getString(R.string.node_package_name), node?.packageName)),
            BindingSearchNodeItem(MCNodeMessage(getString(R.string.node_class_name), node?.className)),
            BindingSearchNodeItem(MCNodeMessage(getString(R.string.node_id), node?.viewIdResourceName)),
            BindingSearchNodeItem(MCNodeMessage("控件编号(id)", idIndex)),
            BindingSearchNodeItem(MCNodeMessage(getString(R.string.node_text), node?.text)),
            BindingSearchNodeItem(MCNodeMessage("控件编号(文本)", txtIndex)),
            BindingSearchNodeItem(MCNodeMessage("层级", depth)),
            BindingSearchNodeItem(MCNodeMessage("坐标(x,y)", "${rect.exactCenterX()},${rect.exactCenterY()}")),
            BindingSearchNodeItem(MCNodeMessage(getString(R.string.node_child_count), node?.childCount)),
            BindingSearchNodeItem(MCNodeMessage(getString(R.string.node_is_enabled), node?.isEnabled)),
            BindingSearchNodeItem(MCNodeMessage(getString(R.string.node_is_focused), node?.isFocused)),
            BindingSearchNodeItem(MCNodeMessage(getString(R.string.node_is_checked), node?.isChecked)),
            BindingSearchNodeItem(MCNodeMessage(getString(R.string.node_is_selected), node?.isSelected)),
            BindingSearchNodeItem(MCNodeMessage(getString(R.string.node_is_visible_to_user), node?.isVisibleToUser)),

            BindingSearchNodeItem(MCNodeMessage(getString(R.string.node_is_clickable), node?.isClickable)),
            BindingSearchNodeItem(MCNodeMessage(getString(R.string.node_is_long_clickable), node?.isLongClickable)),
            BindingSearchNodeItem(
                MCNodeMessage(
                    getString(R.string.node_is_context_clickable),
                    node?.isContextClickable
                )
            ),
            BindingSearchNodeItem(MCNodeMessage(getString(R.string.node_is_checkable), node?.isCheckable)),
            BindingSearchNodeItem(MCNodeMessage(getString(R.string.node_is_editable), node?.isEditable)),
            BindingSearchNodeItem(MCNodeMessage(getString(R.string.node_is_focusable), node?.isFocusable)),
            BindingSearchNodeItem(MCNodeMessage(getString(R.string.node_is_scrollable), node?.isScrollable)),
            BindingSearchNodeItem(MCNodeMessage(getString(R.string.node_is_dismissable), node?.isDismissable)),
            BindingSearchNodeItem(MCNodeMessage(getString(R.string.node_is_content_invalid), node?.isContentInvalid)),
        )

        itemAdapter.add(BindingSearchNodeItem(MCNodeMessage("------- 关联控件 -------", "")))
        node?.parent?.let { itemAdapter.add(BindingSearchNodeItem(MCNodeMessage("父控件", "", it))) }
        repeat(node?.childCount ?: 0) {
            val childNode = node?.getChild(it)
            itemAdapter.add(
                BindingSearchNodeItem(
                    MCNodeMessage(
                        "子控件${it + 1}",
                        childNode?.className,
                        childNode
                    )
                )
            )
        }
    }

}