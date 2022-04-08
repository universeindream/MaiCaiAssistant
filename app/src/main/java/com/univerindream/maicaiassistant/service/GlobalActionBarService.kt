package com.univerindream.maicaiassistant.service

import android.accessibilityservice.AccessibilityService
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.WorkerThread
import cn.hutool.core.thread.ThreadUtil
import com.blankj.utilcode.util.ActivityUtils
import com.elvishew.xlog.XLog
import com.univerindream.maicaiassistant.MHData
import com.univerindream.maicaiassistant.MHUtil
import com.univerindream.maicaiassistant.R
import com.univerindream.maicaiassistant.ui.MainActivity
import com.univerindream.maicaiassistant.utils.NodeUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

class GlobalActionBarService : AccessibilityService() {

    private lateinit var mLayout: FrameLayout

    private var mNotifyId = AtomicInteger()
    private var mWindowStatusChangeTime = AtomicLong(System.currentTimeMillis())
    private var mWindowClassNameByWindowId = mutableMapOf<Int, String>()
    private var mSnapUpStatus = AtomicBoolean(false)
    private var mLoopStartTime = AtomicLong(System.currentTimeMillis())
    private val mCurWindowClassName: String
        get() = mWindowClassNameByWindowId[rootInActiveWindow?.windowId] ?: ""

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
        lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
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
        val packageName = event.packageName.toString()
        val className = event.className.toString()
        val windowId = event.windowId
        val source = event.source

        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                XLog.v(
                    "TYPE_WINDOW_CONTENT_CHANGED - windowId:%s - packageName:%s - className:%s",
                    source?.windowId,
                    packageName,
                    className
                )
            }
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                mWindowStatusChangeTime.set(System.currentTimeMillis())
                XLog.v(
                    "TYPE_WINDOW_STATE_CHANGED - windowId:%s - packageName:%s - className:%s",
                    source?.windowId,
                    packageName,
                    className
                )

                mWindowClassNameByWindowId[windowId] = className
            }
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                XLog.v(
                    "TYPE_VIEW_CLICKED - windowId:%s - packageName:%s - className:%s",
                    source?.windowId,
                    packageName,
                    className
                )
                NodeUtil.log(source)
            }
            else -> {}
        }
    }

    override fun onInterrupt() {}

    private fun runLoop() {
        ThreadUtil.execAsync {
            while (true) {
                if (mSnapUpStatus.get()) {
                    XLog.v("loop - curWindowClassName - $mCurWindowClassName")

                    //校验任务运行时长是否已达标
                    if (System.currentTimeMillis() > mLoopStartTime.get() + MHData.buyMinTime * 1000 * 60) {
                        XLog.i("loop - 任务已执行完 ${MHData.buyMinTime} 分钟，停止运行")
                        cancelTask()
                        MHUtil.notify(
                            mNotifyId.incrementAndGet(),
                            "任务已停止",
                            "任务已执行完 ${MHData.buyMinTime} 分钟，停止运行"
                        )
                        continue
                    }


                    //校验页面是否正常运行中
                    val now = System.currentTimeMillis()
                    val changeTime = mWindowStatusChangeTime.get()
                    when {
                        now - changeTime > 5000 -> {
                            XLog.e("loop - 任务已 >5s 未执行")
                            cancelTask()
                            MHUtil.notify(
                                mNotifyId.incrementAndGet(),
                                "失败提示",
                                "长时间点击无效，运行失败"
                            )
                            if (MHData.wrongAlarmStatus) MHUtil.playRingTone()
                            continue
                        }
                        now - changeTime > 2000 -> {
                            XLog.w("loop - 任务已 >2s 未执行")
                            Thread.sleep(1000)
                        }
                        now - changeTime >= 1000 -> {
                            XLog.w("loop - 任务已 >1s 未执行")
                            Thread.sleep(500)
                        }
                    }

                    doBuy()

                    Thread.sleep(100)
                } else {
                    Thread.sleep(500)
                }
            }
        }

    }

    @WorkerThread
    private fun doBuy() {
        val curClassName = mCurWindowClassName

        when {
            MHUtil.isHomePage(curClassName) -> {
                XLog.v("doBuy - isHomePage")

                MHUtil.toCardTab(rootInActiveWindow)

                when {
                    NodeUtil.isClickByFirstMatchTxt(rootInActiveWindow, "我知道了") -> {
                        XLog.v("loop - isHomePage - 我知道了")
                        NodeUtil.clickByFirstMatchTxt(rootInActiveWindow, "我知道了")
                        mWindowStatusChangeTime.set(System.currentTimeMillis())
                    }
                    NodeUtil.isClickByFirstMatchTxt(rootInActiveWindow, "结算(") -> {
                        //取消，会导致点击失败
                        Thread.sleep(200)
                        XLog.v("loop - isHomePage - 结算(")
                        NodeUtil.clickByFirstMatchTxt(rootInActiveWindow, "结算(")
                    }
                    rootInActiveWindow?.childCount ?: 0 <= 15 -> {
                        XLog.v("loop - isPayPage - 空白页")
                    }
                    else -> {
                        XLog.e("loop - isHomePage - 未知步骤 - childCount:%s", rootInActiveWindow?.childCount)
                        MHUtil.notify(
                            mNotifyId.incrementAndGet(),
                            "未知流程",
                            "需要人工操作"
                        )
                    }
                }
            }
            MHUtil.isPayPage(curClassName) -> {
                XLog.v("doBuy - isPayPage")

                when {
                    NodeUtil.isClickByFirstMatchTxt(rootInActiveWindow, "我知道了") -> {
                        XLog.v("loop - isPayPage - 我知道了")
                        NodeUtil.clickByFirstMatchTxt(rootInActiveWindow, "我知道了")
                    }
                    NodeUtil.isClickByFirstMatchTxt(rootInActiveWindow, "返回购物车") -> {
                        XLog.v("loop - isPayPage - 返回购物车")
                        NodeUtil.clickByFirstMatchTxt(rootInActiveWindow, "返回购物车")
                    }
                    NodeUtil.getByTxtAndFirstMatch(rootInActiveWindow, "选择送达时间") != null -> {
                        XLog.v("loop - isPayPage - 选择送达时间")

                        if (MHData.sendTimeSelectAlarmStatus) {
                            MHUtil.notify(
                                mNotifyId.incrementAndGet(),
                                "选择送达时间流程",
                                "需要人工操作"
                            )

                            MHUtil.playRingTone()
                            cancelTask()
                        }else{
                            val sendTimeList = arrayListOf("09:35-10:30", "10:30-14:30","14:30-18:30","18:30-22:30")
                            val sendTime = sendTimeList.random()
                            NodeUtil.clickByFirstMatchTxt(rootInActiveWindow, sendTime)
                            MHUtil.notify(
                                mNotifyId.incrementAndGet(),
                                "选择送达时间流程",
                                "已自动选择 $sendTime"
                            )
                        }
                    }
                    NodeUtil.clickByFirstMatchTxt(rootInActiveWindow, "支付") -> {
                        XLog.v("loop - isPayPage - 支付")

                        MHUtil.notify(
                            mNotifyId.incrementAndGet(),
                            "支付流程",
                            "需要人工操作"
                        )

                        MHUtil.playRingTone()

                        cancelTask()
                    }
                    rootInActiveWindow?.childCount ?: 0 <= 15 -> {
                        XLog.v("loop - isPayPage - 空白页")
                    }
                    else -> {
                        XLog.e("loop - isPayPage - 未知步骤 - childCount:%s", rootInActiveWindow?.childCount)
                        MHUtil.notify(
                            mNotifyId.incrementAndGet(),
                            "未知流程",
                            "需要人工操作"
                        )
                    }
                }
            }
            else -> {
                XLog.e("doBuy - 未知页面 - %s", curClassName)
            }
        }

    }

    private fun enableTask() {
        mSnapUpStatus.set(true)
        mLoopStartTime.set(System.currentTimeMillis())
        mWindowStatusChangeTime.set(System.currentTimeMillis())
        updateSnapUpButton()

        MHUtil.launchApp()
    }

    private fun cancelTask() {
        mSnapUpStatus.set(false)
        mLoopStartTime.set(-1)
        updateSnapUpButton()
    }

    private fun configureConfigButton() {
        val openConfigButton = mLayout.findViewById<View>(R.id.config) as Button
        openConfigButton.setOnClickListener {
            ActivityUtils.startActivity(MainActivity::class.java)
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
                MHUtil.launchApp()
                enableTask()
            }
        }
    }

    private fun configureOpenAppButton() {
        val stopSnapUpButton = mLayout.findViewById<View>(R.id.open_app) as Button
        stopSnapUpButton.setOnClickListener {
            MHUtil.launchApp()
        }
        stopSnapUpButton.setOnLongClickListener {
            MHUtil.scheduleRingTone()
            return@setOnLongClickListener false
        }
    }

    class SubAlarm

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subAlarm(event: SubAlarm) {
        XLog.d("subAlarm")
        enableTask()
    }

}