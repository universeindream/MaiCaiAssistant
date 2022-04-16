package com.univerindream.maicaiassistant

import android.accessibilityservice.AccessibilityService
import android.app.*
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.app.NotificationCompat
import com.blankj.utilcode.util.*
import com.elvishew.xlog.XLog
import com.univerindream.maicaiassistant.receiver.AlarmReceiver
import com.univerindream.maicaiassistant.service.GlobalActionBarService
import com.univerindream.maicaiassistant.ui.MainActivity
import com.univerindream.maicaiassistant.utils.NodeUtil
import kotlinx.coroutines.delay
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

object MHUtil {

    private var mNotifyId = AtomicInteger()

    /**
     * 校验条件
     */
    fun stepCond(
        rootInActiveWindow: AccessibilityNodeInfo?,
        foregroundClassName: String,
        cond: EMCCond,
        condNode: MCNode
    ): Boolean {
        return when (cond) {
            EMCCond.APP_IS_BACKGROUND -> {
                return rootInActiveWindow?.packageName != condNode.packageName
            }
            EMCCond.EQ_CLASS_NAME -> foregroundClassName == condNode.className
            EMCCond.NODE_EXIST -> {
                return NodeUtil.isExist(rootInActiveWindow, condNode)
            }
            EMCCond.NODE_NO_EXIST -> {
                return !NodeUtil.isExist(rootInActiveWindow, condNode)
            }
            EMCCond.NODE_VISIBLE -> {
                return NodeUtil.isExist(rootInActiveWindow, condNode, EMCMatch.VISIBLE)
            }
            EMCCond.NODE_NO_VISIBLE -> {
                return !NodeUtil.isExist(rootInActiveWindow, condNode, EMCMatch.VISIBLE)
            }
            EMCCond.NODE_SELECTED -> {
                return NodeUtil.isExist(rootInActiveWindow, condNode, EMCMatch.SELECTED_SELF_OR_BROTHER)
            }
            EMCCond.NODE_NOT_SELECTED -> {
                return !NodeUtil.isExist(rootInActiveWindow, condNode, EMCMatch.SELECTED_SELF_OR_BROTHER)
            }
            EMCCond.NODE_CHECKED -> {
                return NodeUtil.isExist(rootInActiveWindow, condNode, EMCMatch.CHECKED_SELF_OR_BROTHER)
            }
            EMCCond.NODE_NOT_CHECKED -> {
                return !NodeUtil.isExist(rootInActiveWindow, condNode, EMCMatch.CHECKED_SELF_OR_BROTHER)
            }
            EMCCond.NODE_CAN_CLICK -> {
                return NodeUtil.isExist(rootInActiveWindow, condNode, EMCMatch.CLICKABLE_SELF_OR_PARENT)
            }
            EMCCond.NODE_NOT_CLICK -> {
                return !NodeUtil.isExist(rootInActiveWindow, condNode, EMCMatch.CLICKABLE_SELF_OR_PARENT)
            }
        }
    }

    suspend fun stepHandle(
        service: AccessibilityService,
        rootInActiveWindow: AccessibilityNodeInfo?,
        handle: MCHandle
    ): Boolean {
        val type = handle.type
        val node = handle.node
        val delay = handle.delay

        val result: Boolean = when (type) {
            EMCHandle.LAUNCH -> {
                node ?: return false
                delay(100)
                service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS)
                delay(100)
                service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
                delay(100)
                AppUtils.launchApp(node.packageName)
                true
            }
            EMCHandle.BACK -> {
                service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
            }
            EMCHandle.RECENTS -> {
                service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS)
            }
            EMCHandle.CLICK_NODE -> {
                node ?: return false
                NodeUtil.clickFirstNode(rootInActiveWindow, node, EMCMatch.CLICKABLE_SELF_OR_PARENT)
            }
            EMCHandle.CLICK_RANDOM_NODE -> {
                node ?: return false
                NodeUtil.clickRandomNode(rootInActiveWindow, node, EMCMatch.CLICKABLE_SELF_OR_PARENT)
            }
            EMCHandle.NONE -> true
            else -> false
        }

        if (result) delay(delay)

        return result
    }

    fun toAccessibilitySetting() =
        ActivityUtils.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))


    /**
     * 是否拥有无障碍权限
     */
    fun hasServicePermission(): Boolean {
        val ct = Utils.getApp()
        val serviceClass = GlobalActionBarService::class.java

        var ok = 0
        try {
            ok = Settings.Secure.getInt(ct.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Settings.SettingNotFoundException) {
        }
        val ms = TextUtils.SimpleStringSplitter(':')
        if (ok == 1) {
            val settingValue = Settings.Secure.getString(
                ct.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                ms.setString(settingValue)
                while (ms.hasNext()) {
                    val accessibilityService = ms.next()
                    if (accessibilityService.contains(serviceClass.simpleName)) {
                        return true
                    }
                }
            }
        }
        return false
    }


    /**
     * 启用前端服务，防止被 kill
     */
    fun startForegroundService(context: Service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                MHConfig.MAI_CAI_ASSISTANT_CHANNEL_ID,
                "Recording Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = context.getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            },
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_ONE_SHOT
            }
        )
        val notification: Notification = NotificationCompat.Builder(context, MHConfig.MAI_CAI_ASSISTANT_CHANNEL_ID)
            .setContentTitle("持续抢购中")
            .setContentText("请勿关闭")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setTicker("ticker")
            .build()
        context.startForeground(100000000, notification)
    }

    fun notify(title: String, content: String) {
        val id = mNotifyId.incrementAndGet()
        val context = Utils.getApp()

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            },
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_NO_CREATE
            }
        )

        NotificationUtils.notify(id) { param ->
            param
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            null
        }
    }


    /**
     * 铃声播放器
     */
    val ringtone: Ringtone by lazy {
        return@lazy RingtoneManager.getRingtone(
            Utils.getApp(),
            RingtoneManager.getActualDefaultRingtoneUri(
                Utils.getApp(),
                RingtoneManager.TYPE_RINGTONE
            )
        )
    }

    fun scheduleRingTone() = if (ringtone.isPlaying) ringtone.stop() else ringtone.play()

    fun playRingTone() {
        if (!ringtone.isPlaying) ringtone.play()
    }

    fun stopRingTone() {
        if (ringtone.isPlaying) ringtone.stop()
    }

    /// 定时相关

    private val alarmPendingIntent: PendingIntent by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                Utils.getApp(),
                0,
                Intent(Utils.getApp(), AlarmReceiver::class.java),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getBroadcast(
                Utils.getApp(),
                0,
                Intent(Utils.getApp(), AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    private fun getAlarmManager() =
        Utils.getApp().getSystemService(AccessibilityService.ALARM_SERVICE) as AlarmManager

    fun enableAlarm(triggerAtMillis: Long): String {
        val friendlyTime = TimeUtils.millis2String(triggerAtMillis)
        XLog.v("enableAlarm - %s", friendlyTime)

        getAlarmManager().cancel(alarmPendingIntent)
        getAlarmManager().set(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            alarmPendingIntent
        )

        return friendlyTime
    }

    fun cancelAlarm() {
        XLog.v("cancelAlarm")
        getAlarmManager().cancel(alarmPendingIntent)
    }

    fun calcNextTime(hour: Int, min: Int): Long {
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, hour)
        c.set(Calendar.MINUTE, min)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)

        if (c.timeInMillis < System.currentTimeMillis()) {
            c.add(Calendar.DAY_OF_YEAR, 1)
        }

        return c.timeInMillis
    }

}