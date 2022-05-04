package com.univerindream.maicaiassistant

import android.accessibilityservice.AccessibilityService
import android.app.*
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import com.blankj.utilcode.util.*
import com.univerindream.maicaiassistant.model.MCSolution
import com.univerindream.maicaiassistant.receiver.AlarmReceiver
import com.univerindream.maicaiassistant.service.MaiCaiAssistantService
import com.univerindream.maicaiassistant.ui.MainActivity
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

object MCUtil {

    private var mNotifyId = AtomicInteger()

    fun toAccessibilitySetting() =
        ActivityUtils.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

    /**
     * 是否拥有无障碍权限
     */
    fun hasServicePermission(): Boolean {
        val ct = Utils.getApp()
        val serviceClass = MaiCaiAssistantService::class.java

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
                MAI_CAI_ASSISTANT_CHANNEL_ID,
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
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification: Notification = NotificationCompat.Builder(context, MAI_CAI_ASSISTANT_CHANNEL_ID)
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
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
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
    private val ringtone: Ringtone by lazy {
        return@lazy RingtoneManager.getRingtone(
            Utils.getApp(),
            RingtoneManager.getActualDefaultRingtoneUri(
                Utils.getApp(),
                RingtoneManager.TYPE_RINGTONE
            )
        )
    }

    fun playRingTone() {
        if (!ringtone.isPlaying) ringtone.play()
    }

    fun stopRingTone() {
        if (ringtone.isPlaying) ringtone.stop()
    }

    /// 定时相关
    private val alarmManager: AlarmManager by lazy {
        Utils.getApp().getSystemService(AccessibilityService.ALARM_SERVICE) as AlarmManager
    }

    private val alarmPendingIntent: PendingIntent by lazy {
        PendingIntent.getBroadcast(
            Utils.getApp(),
            0,
            Intent(Utils.getApp(), AlarmReceiver::class.java),
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun setTimerAlarm(status: Boolean, launchTime: Long) {
        if (status) {
            enableAlarm(launchTime)
            ToastUtils.showLong("将于 ${TimeUtils.millis2String(launchTime, "yyyy/MM/dd HH:mm")} 定时执行")
        } else {
            alarmManager.cancel(alarmPendingIntent)
            ToastUtils.showLong("定时已取消")
        }
    }

    fun enableAlarm(triggerAtMillis: Long) {
        alarmManager.cancel(alarmPendingIntent)
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            alarmPendingIntent
        )
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

    fun isValid(solution: MCSolution): Boolean {
        return try {
            solution.steps.forEach {
                it.handle.type.toStr()
                it.condList.forEach { it.type.toStr() }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getUUID8(uuid: String) = if (uuid.length > 8) uuid.substring(0, 8) else uuid

}