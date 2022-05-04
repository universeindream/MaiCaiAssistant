package com.univerindream.maicaiassistant.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.jeremyliao.liveeventbus.LiveEventBus
import com.univerindream.maicaiassistant.LEBConstants
import com.univerindream.maicaiassistant.MCData
import com.univerindream.maicaiassistant.MCUtil
import com.univerindream.maicaiassistant.R
import com.univerindream.maicaiassistant.databinding.FragmentHomeBinding
import com.univerindream.maicaiassistant.utils.VersionComparator
import com.univerindream.maicaiassistant.viewmodels.HomeViewModel
import com.univerindream.maicaiassistant.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val sharedModel: SharedViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.settingSolutionName.setOnClickListener {
            findNavController().navigate(R.id.action_ConfigFragment_to_localSolutionFragment)
        }
        binding.settingSolutionName.setOnLongClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSolutionFragment())
            false
        }
        binding.settingTimerStatus.setOnCheckedChangeListener { compoundButton, b ->
            if (!compoundButton.isPressed) return@setOnCheckedChangeListener
            viewModel.setTimerStatus(b)
        }
        binding.settingTimerTime.setOnClickListener {
            val timerTime = MCData.timerTime
            val hms = TimeUtils.millis2String(timerTime, "HH:mm").split(":")
            val hour = hms.getOrNull(0)?.ifBlank { "5" }?.toIntOrNull() ?: 5
            val minute = hms.getOrNull(1)?.ifBlank { "55" }?.toIntOrNull() ?: 55
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hour)
                .setMinute(minute)
                .build()
            picker.addOnPositiveButtonClickListener {
                viewModel.setTimerTime(picker.hour, picker.minute)
            }
            picker.show(parentFragmentManager, "timePicker")
        }
        binding.settingHelp.setOnCheckedChangeListener { compoundButton, b ->
            if (!compoundButton.isPressed) return@setOnCheckedChangeListener
            binding.settingHelpInfo.visibility = if (b) View.VISIBLE else View.GONE
        }

        subscribeUi(binding)

        return binding.root
    }

    private fun subscribeUi(binding: FragmentHomeBinding) {
        sharedModel.curSolution.observe(viewLifecycleOwner) {
            binding.settingSolutionName.text = it.name
        }
        viewModel.timerStatus.observe(viewLifecycleOwner) {
            binding.settingTimerStatus.isChecked = it
        }
        viewModel.timerTime.observe(viewLifecycleOwner) {
            binding.settingTimerTime.text = TimeUtils.millis2String(it, "yyyy/MM/dd HH:mm")
        }
        viewModel.hasAssistantPermission.observe(viewLifecycleOwner) {
            if (true == it) return@observe
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("提示")
                .setMessage("请先开启无障碍服务!!!")
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton("去开启") { _, _ ->
                    MCUtil.toAccessibilitySetting()
                }
                .setCancelable(false)
                .show()
        }
        viewModel.latestReleases.observe(viewLifecycleOwner) {
            if (it.isSuccess) {
                val githubVersion = it.getOrNull()?.firstOrNull()?.tag_name ?: return@observe
                val curVersion = "v" + AppUtils.getAppVersionName()
                LogUtils.v(curVersion, githubVersion)

                if (VersionComparator.INSTANCE.compare(curVersion, githubVersion) < 0) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("新版本提示")
                        .setMessage("$githubVersion 已发布，请及时更新")
                        .setPositiveButton("下载") { _, _ ->
                            val uri: Uri =
                                Uri.parse("https://github.com/universeindream/MaiCaiAssistant/releases/latest/download/app-release.apk")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        }
                        .setNegativeButton("放弃") { a, _ ->
                            a.cancel()
                        }
                        .setCancelable(false)
                        .show()
                }
            } else {
                LogUtils.e(it.exceptionOrNull())
            }
        }

        LiveEventBus.get<String>(LEBConstants.REFRESH_TIMER_STATUS).observe(viewLifecycleOwner) {
            viewModel.updateTimerStatus()
        }
    }
}