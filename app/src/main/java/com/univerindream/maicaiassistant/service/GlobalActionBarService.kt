package com.univerindream.maicaiassistant.service

import android.accessibilityservice.AccessibilityService
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

class GlobalActionBarService : AccessibilityService() {

    private lateinit var mLayout: FrameLayout

    private var mSnapUpStatus = AtomicBoolean(false)
    private var mLoopStartTime = AtomicLong(System.currentTimeMillis())

    private var mHandleLog: MCHandleLog? = null

    private var mForegroundPackageName: String = ""
    private var mForegroundClassName: String = ""
    private var mForegroundWindowId: Int = -1

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
        val pName = event.packageName.toString()
        val cName = event.className.toString()
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
        GlobalScope.launch {
            while (true) {
                if (mSnapUpStatus.get()) {
                    XLog.v("loop - curWindowClassName - $mCurClassNameByRootWindow")

                    //任务时长是否达标
                    if (System.currentTimeMillis() > mLoopStartTime.get() + MHData.buyMinTime * 1000 * 60) {
                        XLog.i("loop - 任务已执行完 ${MHData.buyMinTime} 分钟，停止运行")
                        cancelTask()
                        MHUtil.notify(
                            "任务已停止",
                            "任务已执行完 ${MHData.buyMinTime} 分钟，停止运行"
                        )
                        continue
                    }

                    //异常检查
                    val now = System.currentTimeMillis()
                    val handleTime = mHandleLog?.handleTime ?: now
                    when {
                        now - handleTime > 10000 -> {
                            XLog.e("流程 - ${mHandleLog?.stepName} 已超过 10s 未执行")
                            cancelTask()
                            MHUtil.notify(
                                "异常提示",
                                "流程 - ${mHandleLog?.stepName} 已重复执行 10s 未执行"
                            )
                            ToastUtils.showLong("异常提示 - 流程 - ${mHandleLog?.stepName} 已重复执行 10s 未执行")
                            if (MHData.wrongAlarmStatus) MHUtil.playRingTone()
                            continue
                        }
                    }

                    //操作流程
                    for (step in MHConfig.curMHSolution.steps) {
                        val condResult = step.condList.all {
                            MHUtil.stepCond(
                                rootInActiveWindow,
                                mForegroundClassName,
                                it.type,
                                it.node
                            )
                        }
                        XLog.v("steps - ${step.name} - $condResult")

                        if (condResult) {
                            mHandleLog = mHandleLog ?: MCHandleLog(step.name, System.currentTimeMillis())
                            if (step.name != mHandleLog!!.stepName) {
                                mHandleLog!!.stepName = step.name
                                mHandleLog!!.handleTime = System.currentTimeMillis()
                            }

                            MHUtil.stepHandle(this@GlobalActionBarService, rootInActiveWindow, step.handle)

                            if (step.isAlarm) {
                                MHUtil.notify(
                                    "${step.name} 流程",
                                    "触发响铃"
                                )
                                MHUtil.playRingTone()
                            }

                            if (step.isManual) {
                                MHUtil.notify(
                                    "${step.name} 流程",
                                    "需要人工操作"
                                )
                                cancelTask()
                            }
                            break
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
        mHandleLog = null
        updateSnapUpButton()

        ToastUtils.showShort("任务启动中...")
    }

    private fun cancelTask() {
        mSnapUpStatus.set(false)
        mLoopStartTime.set(-1)
        updateSnapUpButton()
    }

    private fun configureConfigButton() {
        val openConfigButton = mLayout.findViewById<View>(R.id.config) as Button
        openConfigButton.setOnClickListener {
//            AppUtils.launchApp(AppUtils.getAppPackageName())

            val launchIntent = packageManager.getLaunchIntentForPackage(AppUtils.getAppPackageName())
            launchIntent?.let { startActivity(it) }
        }
    }

    private fun updateSnapUpButton() {
        val snapUpButton = mLayout.findViewById<View>(R.id.snapUp) as Button
        snapUpButton.text = if (mSnapUpStatus.get()) "取消" else "抢购"
    }

    private fun configureSnapUpButton() {
        val snapUpButton = mLayout.findViewById<View>(R.id.snapUp) as Button
        snapUpButton.setOnClickListener {
            if (mSnapUpStatus.get()) {
                cancelTask()
            } else {
                enableTask()
            }
        }
    }

    private fun configureOpenAppButton() {
        val openAppButton = mLayout.findViewById<View>(R.id.open_app) as Button
        if (BuildConfig.DEBUG) openAppButton.visibility = View.VISIBLE
        openAppButton.setOnClickListener {

        }
    }

    class SubAlarm

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subAlarm(event: SubAlarm) {
        XLog.d("subAlarm")
        enableTask()
    }

}