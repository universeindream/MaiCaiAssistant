package com.univerindream.maicaiassistant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import cn.hutool.core.date.DateUtil
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.elvishew.xlog.XLog
import com.univerindream.maicaiassistant.MHData
import com.univerindream.maicaiassistant.MHUtil
import com.univerindream.maicaiassistant.R
import com.univerindream.maicaiassistant.widget.TimePickerFragment
import com.univerindream.maicaiassistant.databinding.FragmentConfigBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ConfigFragment : Fragment() {

    private var _binding: FragmentConfigBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        EventBus.getDefault().register(this)

        binding.settingPanel.setOnTouchListener { view, motionEvent ->
            KeyboardUtils.hideSoftInput(view)
            view.requestFocus()
            return@setOnTouchListener false
        }

        binding.settingBuyPlatform.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.setting_buy_meituanmaicai -> MHData.buyPlatform = 1
                R.id.setting_buy_dingdong -> MHData.buyPlatform = 2
            }
        }
        binding.settingBuyTimeValue.addTextChangedListener({ _, _, _, _ -> }, { _, _, _, _ -> }) { s ->
            s?.toString()?.let {
                if (it.isNotBlank()) MHData.buyMinTime = it.toInt() else MHData.buyMinTime = 25
            }
        }


        binding.settingTimerTriggerStatus.setOnCheckedChangeListener { compoundButton, b ->
            if (!compoundButton.isPressed) return@setOnCheckedChangeListener

            MHData.timerTriggerStatus = b

            if (b) {
                if (MHData.timerTriggerStatus) {
                    MHUtil.enableAlarm()
                    ToastUtils.showShort("将于 ${DateUtil.formatDateTime(Date(MHData.timerTriggerTime))} 开启定时抢购")
                } else {
                    ToastUtils.showShort("请设置未来时间")
                }
            } else {
                MHUtil.cancelAlarm()
                ToastUtils.showShort("定时抢购已取消")
            }
        }
        binding.settingTimerTriggerValue.text =
            DateUtil.formatDateTime(Date(MHData.timerTriggerTime))
        binding.settingTimerTriggerChange.setOnClickListener {
            TimePickerFragment { _, h, m ->
                val nextTime = MHUtil.calcNextTime(h, m)
                MHData.timerTriggerTime = nextTime
                binding.settingTimerTriggerValue.text = DateUtil.formatDateTime(Date(nextTime))
                if (MHData.timerTriggerStatus) MHUtil.enableAlarm(nextTime)
            }.show(parentFragmentManager, "timePicker")
        }

        binding.settingSendTimeSeleectAlarmStatus.setOnCheckedChangeListener { compoundButton, b ->
            if (!compoundButton.isPressed) return@setOnCheckedChangeListener

            MHData.sendTimeSelectAlarmStatus = b

            if (MHData.sendTimeSelectAlarmStatus) {
                ToastUtils.showShort("送达时间响铃已开启")
            } else {
                ToastUtils.showShort("送达时间响铃已关闭")
            }
        }
        binding.settingWrongAlarmStatus.setOnCheckedChangeListener { compoundButton, b ->
            if (!compoundButton.isPressed) return@setOnCheckedChangeListener

            MHData.wrongAlarmStatus = b

            if (MHData.wrongAlarmStatus) {
                ToastUtils.showShort("异常响铃已开启")
            } else {
                ToastUtils.showShort("异常响铃已关闭")
            }
        }


        binding.settingPermission.setOnCheckedChangeListener { compoundButton, b ->
            if (!compoundButton.isPressed) return@setOnCheckedChangeListener

            if (b) {
                MHUtil.toAccessibilitySetting()
            } else {
                ToastUtils.showShort("请到系统设置页关闭")
            }
        }
        binding.settingRingtoneStatus.setOnClickListener {
            if (MHUtil.ringtone.isPlaying) MHUtil.ringtone.stop() else MHUtil.ringtone.play()
        }

        loadData()
    }

    override fun onResume() {
        super.onResume()

        loadData()
    }

    private fun loadData() {
        binding.settingPermission.isChecked = MHUtil.hasServicePermission()


        binding.settingBuyPlatform.check(if (MHData.buyPlatform == 1) R.id.setting_buy_meituanmaicai else R.id.setting_buy_dingdong)
        binding.settingBuyTimeValue.setText(MHData.buyMinTime.toString())

        binding.settingTimerTriggerStatus.isChecked = MHData.timerTriggerStatus

        binding.settingSendTimeSeleectAlarmStatus.isChecked = MHData.sendTimeSelectAlarmStatus
        binding.settingWrongAlarmStatus.isChecked = MHData.wrongAlarmStatus

        binding.settingRingtoneStatus.isChecked = MHUtil.ringtone.isPlaying
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        EventBus.getDefault().unregister(this)
    }

    class SubRefresh

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun subRefresh(refresh: SubRefresh) {
        XLog.v("subRefresh")
        loadData()
    }
}