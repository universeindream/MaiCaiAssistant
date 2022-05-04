package com.univerindream.maicaiassistant.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.univerindream.maicaiassistant.LEBConstants
import com.univerindream.maicaiassistant.MCUtil
import com.univerindream.maicaiassistant.data.DataRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var dataRepository: DataRepository

    override fun onReceive(p0: Context?, p1: Intent?) {
        LogUtils.v("AlarmReceiver - onReceive")
        dataRepository.timerStatus = false
        LiveEventBus.get<String>(LEBConstants.REFRESH_TIMER_STATUS).post("")

        if (MCUtil.hasServicePermission()) {
            ToastUtils.showLong("定时执行中...")
            LiveEventBus.get<String>(LEBConstants.LAUNCH_SOLUTION).post("")
        } else {
            ToastUtils.showLong("未开启无障碍服务，定时执行失败")
            MCUtil.notify("失败提示", "未开启无障碍服务，定时执行失败")
        }
    }
}