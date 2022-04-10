package com.univerindream.maicaiassistant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import cn.hutool.core.date.DateUtil
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.elvishew.xlog.XLog
import com.univerindream.maicaiassistant.MHConfig
import com.univerindream.maicaiassistant.MHData
import com.univerindream.maicaiassistant.MHUtil
import com.univerindream.maicaiassistant.R
import com.univerindream.maicaiassistant.databinding.FragmentConfigBinding
import com.univerindream.maicaiassistant.widget.TimePickerFragment
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

        binding.settingChooseAuto.setOnClickListener {
            //findNavController().navigate(R.id.action_ConfigFragment_to_HelpFragment)

            val builder = AlertDialog.Builder(this@ConfigFragment.requireContext())
            builder.setTitle(R.string.dialog_choose_solution)
                .setItems(
                    MHConfig.allMHSolution.map { it.name }.toTypedArray()
                ) { _, which ->
                    MHConfig.curMHSolution = MHConfig.allMHSolution[which]
                    loadData()
                }
            builder.create().show()

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
                val timerTime = MHData.timerTriggerTime
                if (timerTime > System.currentTimeMillis()) {
                    MHUtil.enableAlarm(timerTime)
                    ToastUtils.showShort("将于 ${DateUtil.formatDateTime(Date(timerTime))} 开启定时抢购")
                } else {
                    MHData.timerTriggerStatus = false
                    compoundButton.isChecked = false
                    ToastUtils.showShort("请设置未来时间")
                }
            } else {
                MHUtil.cancelAlarm()
                ToastUtils.showShort("定时抢购已取消")
            }
        }
        binding.settingTimerTriggerValue.text = DateUtil.formatDateTime(Date(MHData.timerTriggerTime))
        binding.settingTimerTriggerChange.setOnClickListener {
            TimePickerFragment { _, h, m ->
                val nextTime = MHUtil.calcNextTime(h, m)
                MHData.timerTriggerTime = nextTime
                binding.settingTimerTriggerValue.text = DateUtil.formatDateTime(Date(nextTime))
                if (MHData.timerTriggerStatus) {
                    MHUtil.enableAlarm(nextTime)
                    ToastUtils.showShort("将于 ${DateUtil.formatDateTime(Date(MHData.timerTriggerTime))} 开启定时抢购")
                }
            }.show(parentFragmentManager, "timePicker")
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

        binding.settingAutoInfo.text = MHConfig.curMHSolution.name

        binding.settingBuyTimeValue.setText(MHData.buyMinTime.toString())

        binding.settingTimerTriggerStatus.isChecked = MHData.timerTriggerStatus
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