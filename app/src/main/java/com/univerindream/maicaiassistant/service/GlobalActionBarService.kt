package com.univerindream.maicaiassistant.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.annotation.TargetApi
import android.graphics.Path
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.Button
import android.widget.FrameLayout
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ToastUtils
import com.elvishew.xlog.XLog
import com.univerindream.maicaiassistant.*
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong


class GlobalActionBarService : AccessibilityService() {

    private lateinit var mLayout: FrameLayout

    private var mSnapUpStatus = AtomicBoolean(false)
    private var mLoopStartTime = AtomicLong(System.currentTimeMillis())

    private var mStepExecuteTime = linkedMapOf<MCStep, Long>()

    private var mForegroundPackageName: String = ""
    private var mForegroundClassName: String = ""
    private var mForegroundWindowId: Int = -1

    private val mServiceScope = CoroutineScope(Dispatchers.Main + Job())

    private var mClassNameByWindowId = mutableMapOf<Int, String>()
    private val mCurClassNameByRootWindow: String
        get() = mClassNameByWindowId[rootInActiveWindow?.windowId] ?: ""

    override fun onCreate() {
        super.onCreate()

        MHUtil.startForegroundService(this)
    }

    override fun onServiceConnected() {
        XLog.v("onServiceConnected")

        // Create an overlay and display the action bar
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        mLayout = FrameLayout(this)
        val lp = WindowManager.LayoutParams()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
        } else {
            lp.type = WindowManager.LayoutParams.TYPE_PHONE
        }

        lp.format = PixelFormat.TRANSLUCENT
        lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER_VERTICAL or Gravity.END
        val inflater = LayoutInflater.from(this)
        inflater.inflate(R.layout.action_bar, mLayout)
        wm.addView(mLayout, lp)

        configureConfigButton()
        configureSnapUpButton()
        configureOpenAppButton()

        EventBus.getDefault().register(this)

        runLoop()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val pName = event.packageName?.toString() ?: ""
        val cName = event.className?.toString() ?: ""
        val windowId = event.windowId
        val source = event.source

        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                XLog.v("TYPE_WINDOW_CONTENT_CHANGED - %s - %s", event, source)
            }
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                XLog.v("TYPE_WINDOW_STATE_CHANGED - %s - %s", event, source)
                if (pName == "com.android.systemui" || pName == "android") return
                mForegroundPackageName = pName
                mForegroundClassName = cName
                mForegroundWindowId = windowId
                mClassNameByWindowId[windowId] = cName
            }
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                XLog.v("TYPE_VIEW_CLICKED - %s - %s", event, source)
            }
            else -> {}
        }
    }

    override fun onInterrupt() {}

    private fun runLoop() {
        mServiceScope.launch {
            while (true) {
                if (mSnapUpStatus.get()) {
                    XLog.v("loop - curWindowClassName - $mCurClassNameByRootWindow")

                    //任务时长是否达标
                    if (System.currentTimeMillis() > mLoopStartTime.get() + MHData.buyMinTime * 1000 * 60) {
                        val message = "已执行了 ${MHData.buyMinTime} 分钟"
                        XLog.i("loop - %s", message)
                        MHUtil.notify("完成提示", message)
                        cancelTask()
                        continue
                    }

                    //循环执行步骤
                    val steps = MHConfig.curMCSolution.steps.filter { it.isEnable }
                    for (step in steps) {
                        val isMatchCond = step.condList.all {
                            MHUtil.stepCond(
                                rootInActiveWindow,
                                mForegroundPackageName,
                                mForegroundClassName,
                                it.type,
                                it.node
                            )
                        }
                        if (!isMatchCond) continue
                        XLog.v("steps - ${step.name}")

                        //步骤只执行一次
                        if (step.isExecuteOnce && mStepExecuteTime.containsKey(step)) {
                            continue
                        }

                        //步骤警报
                        if (step.isAlarm) {
                            MHUtil.notify("响铃提示", "\"${step.name}\" 步骤，触发响铃")
                            MHUtil.playRingTone()
                        }

                        //步骤人工
                        if (step.isManual) {
                            MHUtil.notify("人工提示", "\"${step.name}\" 步骤，人工操作")
                            cancelTask()
                            break
                        }

                        val handleResult =
                            MHUtil.stepHandle(this@GlobalActionBarService, rootInActiveWindow, step.handle)
                        XLog.v("steps - ${step.name} - 执行结果 - $handleResult")

                        //步骤失败返回
                        if (step.isFailBack && !handleResult) {
                            mStepExecuteTime.remove(step)
                            mStepExecuteTime[step] = System.currentTimeMillis()
                            repeat(step.failBackCount) {
                                performGlobalAction(GLOBAL_ACTION_BACK)
                                delay(200)
                            }
                            continue
                        }

                        //步骤重复
                        if (step.isRepeat) {
                            mStepExecuteTime[step] = System.currentTimeMillis()
                        } else {
                            val logStep = mStepExecuteTime.keys.lastOrNull()
                            if (logStep != step) {
                                mStepExecuteTime.remove(step)
                                mStepExecuteTime[step] = System.currentTimeMillis()
                            }

                            val executeTime = mStepExecuteTime[step]!!
                            if (executeTime + 10 * 1000 < System.currentTimeMillis()) {
                                val message = "\"${step.name}\" 步骤执行失败，请删除或修改该步骤!!!"
                                XLog.e("${logStep?.name} %s", message)
                                MHUtil.notify("失败提示", message)
                                ToastUtils.showLong(message)
                                if (MHData.wrongAlarmStatus) MHUtil.playRingTone()
                                cancelTask()
                                break
                            }

                            if (step == steps.last()) {
                                MHUtil.notify("终止提示", "\"${step.name}\" 为最后步骤，终止任务")
                                cancelTask()
                            }
                        }
                    }

                    delay(100)
                } else {
                    delay(1000)
                }
            }
        }

    }

    private fun enableTask() {
        mSnapUpStatus.set(true)
        mLoopStartTime.set(System.currentTimeMillis())
        mStepExecuteTime.clear()
        updateSnapUpButton()
    }

    private fun cancelTask() {
        mSnapUpStatus.set(false)
        updateSnapUpButton()
    }

    private fun configureConfigButton() {
        val openConfigButton = mLayout.findViewById<View>(R.id.btn_config) as Button
        openConfigButton.setOnClickListener {
            AppUtils.launchApp(AppUtils.getAppPackageName())
        }
    }

    private fun updateSnapUpButton() {
        val snapUpButton = mLayout.findViewById<View>(R.id.btn_snap_up) as Button
        val targetTxt = if (mSnapUpStatus.get()) "取消" else "抢购"
        if (targetTxt == snapUpButton.text) return
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                snapUpButton.text = targetTxt
            }
        }
    }

    private fun configureSnapUpButton() {
        val snapUpButton = mLayout.findViewById<View>(R.id.btn_snap_up) as Button
        snapUpButton.setOnClickListener {
            if (mSnapUpStatus.get()) {
                cancelTask()
            } else {
                enableTask()
            }
        }
    }

    private fun configureOpenAppButton() {
        val openAppButton = mLayout.findViewById<View>(R.id.btn_test) as Button
        if (BuildConfig.DEBUG) openAppButton.visibility = View.VISIBLE
        openAppButton.setOnClickListener {
            AppUtils.launchApp("com.yaya.zone")
        }
        openAppButton.setOnLongClickListener {
            startActivity(packageManager.getLaunchIntentForPackage("com.yaya.zone"))
            return@setOnLongClickListener false
        }
    }

    suspend fun test() = withContext(Dispatchers.Main) {
        XLog.v("循环中")
        if (rootInActiveWindow?.findAccessibilityNodeInfosByText("结算")?.isNotEmpty() == true) {
            XLog.i("test - 点击 结算")
            click(820f, 2090f)
            delay(200)
        } else if (rootInActiveWindow?.findAccessibilityNodeInfosByText("我知道了")?.isNotEmpty() == true) {
            XLog.i("test - 点击 我知道了")
            click(556f, 1314f)
            delay(200)
        } else if (rootInActiveWindow?.findAccessibilityNodeInfosByText("返回购物车")?.isNotEmpty() == true) {
            XLog.i("test - 点击 返回购物车")
            click(533f, 1300f)
            delay(200)
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun click(x: Float, y: Float) {
        val builder = GestureDescription.Builder()
        val path = Path()
        path.moveTo(x, y)
        path.lineTo(x, y)
        builder.addStroke(StrokeDescription(path, 1, 1))
        dispatchGesture(builder.build(), object : GestureResultCallback() {
            override fun onCancelled(gestureDescription: GestureDescription) {
                super.onCancelled(gestureDescription)
            }

            override fun onCompleted(gestureDescription: GestureDescription) {
                super.onCompleted(gestureDescription)
            }
        }, null)
    }

    class SubAlarm

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subAlarm(event: SubAlarm) {
        XLog.d("subAlarm")
        enableTask()
    }

}