package com.univerindream.maicaiassistant.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.elvishew.xlog.XLog
import com.univerindream.maicaiassistant.MHData
import com.univerindream.maicaiassistant.service.GlobalActionBarService
import com.univerindream.maicaiassistant.ui.ConfigFragment
import org.greenrobot.eventbus.EventBus

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        XLog.v("AlarmReceiver - onReceive")
        MHData.timerTriggerStatus = false
        EventBus.getDefault().post(GlobalActionBarService.SubAlarm())
        EventBus.getDefault().post(ConfigFragment.SubRefresh())
    }
}