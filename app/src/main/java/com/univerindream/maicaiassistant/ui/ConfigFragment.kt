package com.univerindream.maicaiassistant.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.*
import com.elvishew.xlog.XLog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.univerindream.maicaiassistant.*
import com.univerindream.maicaiassistant.api.GithubApi
import com.univerindream.maicaiassistant.databinding.FragmentConfigBinding
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.FileOutputStream

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ConfigFragment : Fragment() {

    private var _binding: FragmentConfigBinding? = null

    private val binding get() = _binding!!

    private val exportARLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                val result = activityResult.data?.data.toString()
                XLog.i(result)
                try {
                    val uri = Uri.parse(result)
                    Utils.getApp().contentResolver.openFileDescriptor(uri, "w")?.use {
                        FileOutputStream(it.fileDescriptor).use {
                            it.write(GsonUtils.toJson(MHConfig.curMCSolution).toByteArray())
                        }
                    }
                    ToastUtils.showShort("方案导出成功")
                } catch (e: Exception) {
                    ToastUtils.showLong("方案导出失败")
                }
            }
        }
    private val importARLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                val result = activityResult.data?.data.toString()
                XLog.i(result)

                try {
                    val json = FileIOUtils.readFile2String(UriUtils.uri2File(Uri.parse(result)))
                    MHConfig.curMCSolution = GsonUtils.fromJson(json, MCSolution::class.java)
                    ToastUtils.showLong("方案导入成功")
                    loadData()
                } catch (e: Exception) {
                    ToastUtils.showLong("方案导入失败")
                }

            }
        }

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

        binding.settingEdit.setOnClickListener {
            findNavController().navigate(R.id.action_ConfigFragment_to_SolutionFragment)
        }
        binding.settingChoose.setOnClickListener {
            val solutions = arrayListOf<MCSolution>()
            solutions.addAll(MHDefault.defaultMCSolutions)
            solutions.addAll(MHDefault.githubSolutions)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("默认方案 - 选后会覆盖当前方案")
                .setItems(solutions.map { it.name }.toTypedArray()) { _, which ->
                    MHConfig.curMCSolution = solutions[which]
                    loadData()
                }
                .show()
        }
        binding.settingEditJson.setOnClickListener {
            findNavController().navigate(R.id.action_ConfigFragment_to_JsonFragment)
        }
        binding.settingImport.setOnClickListener {
            importARLauncher.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "application/json"
                addCategory(Intent.CATEGORY_OPENABLE)
            })
        }
        binding.settingExport.setOnClickListener {
            exportARLauncher.launch(Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                val fileName = "${BuildConfig.VERSION_NAME}_${MHConfig.curMCSolution.name}_${
                    TimeUtils.millis2String(
                        System.currentTimeMillis(),
                        "yyyyMMddHHmm"
                    )
                }.json"
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/json"
                putExtra(Intent.EXTRA_TITLE, fileName)
            })
        }

        binding.settingBuyTimeValue.editText?.doAfterTextChanged { inputText ->
            val time = if (inputText.isNullOrBlank()) 25 else inputText.toString().toInt()
            MHData.buyMinTime = time
        }

        binding.settingTimerTriggerStatus.setOnCheckedChangeListener { compoundButton, b ->
            if (!compoundButton.isPressed) return@setOnCheckedChangeListener

            if (b) {
                val timerTime = MHData.timerTriggerTime
                if (timerTime > System.currentTimeMillis()) {
                    MHUtil.enableAlarm(timerTime)
                    ToastUtils.showShort("将于 ${TimeUtils.millis2String(timerTime)} 开启定时抢购")
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
        binding.settingTimerTriggerValue.text = TimeUtils.millis2String(MHData.timerTriggerTime)
        binding.settingTimerTriggerChange.setOnClickListener {
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(5)
                .setMinute(50)
                .build()
            picker.addOnPositiveButtonClickListener {
                val nextTime = MHUtil.calcNextTime(picker.hour, picker.minute)
                MHData.timerTriggerTime = nextTime
                binding.settingTimerTriggerValue.text = TimeUtils.millis2String(nextTime)
                if (MHData.timerTriggerStatus) {
                    MHUtil.enableAlarm(nextTime)
                    ToastUtils.showLong("将于 ${TimeUtils.millis2String(MHData.timerTriggerTime)} 开启定时抢购")
                }
            }
            picker.show(parentFragmentManager, "timePicker")
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
        binding.settingKeepScreen.setOnCheckedChangeListener { compoundButton, b ->
            if (!compoundButton.isPressed) return@setOnCheckedChangeListener

            if (b) {
                requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }

        loadData()

        if (!MHUtil.hasServicePermission()) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("提示")
                .setMessage("请先开启无障碍权限")
                .setNegativeButton("取消") { dialog, which ->
                    dialog.cancel()
                }
                .setPositiveButton("去开启") { dialog, which ->
                    MHUtil.toAccessibilitySetting()
                }
                .setCancelable(false)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()

        loadData()
        checkTimerTrigger()
        checkConfig()
    }

    private fun loadData() {
        binding.settingPermission.isChecked = MHUtil.hasServicePermission()

        binding.settingAutoInfo.text = MHConfig.curMCSolution.name

        binding.settingBuyTimeValue.editText?.setText(MHData.buyMinTime.toString())

        binding.settingTimerTriggerStatus.isChecked = MHData.timerTriggerStatus
        binding.settingWrongAlarmStatus.isChecked = MHData.wrongAlarmStatus

        binding.settingRingtoneStatus.isChecked = MHUtil.ringtone.isPlaying
    }

    private fun checkTimerTrigger() {
        if (MHData.timerTriggerStatus && MHData.timerTriggerTime < System.currentTimeMillis()) {
            MHUtil.cancelAlarm()
        }
    }

    private fun checkConfig() {
        lifecycleScope.launch {
            try {
                val json = GithubApi.get()
                    .downloadFileWithDynamicUrlSync("https://raw.githubusercontent.com/universeindream/MaiCaiAssistant/main/config.json")
                    .string()
                val solution =
                    Gson().fromJson<List<MCSolution>>(json, object : TypeToken<ArrayList<MCSolution>>() {}.type)
                MHDefault.githubSolutions.clear()
                MHDefault.githubSolutions.addAll(solution)
                XLog.i("远程方案更新成功")
            } catch (e: Exception) {
                XLog.e(e)
            }
        }
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