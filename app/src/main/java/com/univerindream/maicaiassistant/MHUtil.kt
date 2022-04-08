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
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import cn.hutool.core.date.DateUtil
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.NotificationUtils
import com.blankj.utilcode.util.Utils
import com.elvishew.xlog.XLog
import com.univerindream.maicaiassistant.receiver.AlarmReceiver
import com.univerindream.maicaiassistant.service.GlobalActionBarService
import com.univerindream.maicaiassistant.ui.MainActivity
import com.univerindream.maicaiassistant.utils.NodeUtil
import java.util.*

object MHUtil {

    fun launchApp() {
        when (MHData.buyPlatform) {
            1 -> {
                AppUtils.launchApp(MHConfig.MT_PACKAGE_NAME)
            }
            2 -> {
                AppUtils.launchApp(MHConfig.DD_PACKAGE_NAME)
            }
        }
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
                MHConfig.MALL_HELP_CHANNEL_ID,
                "Recording Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = context.getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0, notificationIntent, 0
        )
        val notification: Notification =
            NotificationCompat.Builder(context, MHConfig.MALL_HELP_CHANNEL_ID)
                .setContentTitle("持续抢购中")
                .setContentText("请勿关闭")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build()
        context.startForeground(1, notification)
    }


    fun toCardTab(root: AccessibilityNodeInfo?) {
        root ?: return
        when (MHData.buyPlatform) {
            1 -> {
                val cartNode = NodeUtil.getByIdAndFirstMatch(root, MHConfig.MT_Tab_Cart)
                if (cartNode?.isSelected != true) {
                    NodeUtil.click(cartNode)
                }
            }
            2 -> {
                val cartNode = NodeUtil.getByIdAndFirstMatch(root, MHConfig.DD_Tab_Cart)
                if (cartNode?.isSelected != true) {
                    NodeUtil.click(cartNode)
                }
            }
        }
    }

    fun isHomePage(className: String) = when (MHData.buyPlatform) {
        1 -> {
            MHConfig.MT_CLASS_HOME == className
        }
        2 -> {
            MHConfig.DD_CLASS_HOME == className
        }
        else -> false
    }


    fun isPayPage(className: String) = when (MHData.buyPlatform) {
        1 -> {
            MHConfig.MT_CLASS_PAY == className
        }
        2 -> {
            MHConfig.DD_CLASS_PAY == className
        }
        else -> false
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
        if (!ringtone.isPlaying)  ringtone.play()
    }
    fun stopRingTone(){
        if (ringtone.isPlaying)  ringtone.stop()
    }

    /// 定时相关

    private val alarmPendingIntent: PendingIntent by lazy {
        PendingIntent.getBroadcast(
            Utils.getApp(),
            0,
            Intent(Utils.getApp(), AlarmReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getAlarmManager() =
        Utils.getApp().getSystemService(AccessibilityService.ALARM_SERVICE) as AlarmManager

    fun enableAlarm(triggerAtMillis: Long = MHData.timerTriggerTime): String {
        val friendlyTime = DateUtil.formatDateTime(Date(triggerAtMillis))
        XLog.v("enableAlarm - %s", friendlyTime)

        MHData.timerTriggerTime = triggerAtMillis
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

    @RequiresApi(Build.VERSION_CODES.P)
    fun screenshot(service: AccessibilityService) {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_TAKE_SCREENSHOT)
    }

    fun notify(id: Int, title: String, content: String) {
        NotificationUtils.notify(id) { param ->
            param.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(
                    PendingIntent.getActivity(
                        Utils.getApp(),
                        0,
                        Intent().putExtra("id", id),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                .setAutoCancel(true)
            null
        }
    }

}