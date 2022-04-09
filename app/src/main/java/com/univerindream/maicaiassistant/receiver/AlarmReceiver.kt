package com.univerindream.maicaiassistant.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.ToastUtils
import com.elvishew.xlog.XLog
import com.univerindream.maicaiassistant.MHData
import com.univerindream.maicaiassistant.MHUtil
import com.univerindream.maicaiassistant.service.GlobalActionBarService
import com.univerindream.maicaiassistant.ui.ConfigFragment
import org.greenrobot.eventbus.EventBus

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        XLog.v("AlarmReceiver - onReceive")
        MHData.timerTriggerStatus = false

        if (MHUtil.hasServicePermission()) {
            EventBus.getDefault().post(GlobalActionBarService.SubAlarm())
        } else {
            ToastUtils.showLong("暂为开启无障碍模式，定时任务无法执行")
            MHUtil.notify("异常", "暂为开启无障碍模式，定时任务无法执行")
        }

        EventBus.getDefault().post(ConfigFragment.SubRefresh())
    }
}