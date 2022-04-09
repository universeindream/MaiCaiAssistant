package com.univerindream.maicaiassistant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.hutool.json.JSONUtil
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.univerindream.maicaiassistant.MCStep
import com.univerindream.maicaiassistant.MHConfig
import com.univerindream.maicaiassistant.MHData
import com.univerindream.maicaiassistant.databinding.FragmentHelpBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HelpFragment : Fragment() {

    private var _binding: FragmentHelpBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.helpSetDefault.setOnClickListener {
            MHConfig.setDefaultStepsData()

            loadData()
        }
        binding.helpSave.setOnClickListener {
            val data = binding.helpSteps.text.toString()

            if (JSONUtil.isTypeJSONArray(data)) {
                val steps = Gson().fromJson<List<MCStep>>(
                    MHData.stepsJsonMeiTuan,
                    object : TypeToken<ArrayList<MCStep>>() {}.type
                )
                when (MHData.buyPlatform) {
                    1 -> {
                        MHData.stepsJsonMeiTuan = data
                        MHConfig.mtSteps = steps
                    }
                    else -> {
                        MHData.stepsJsonDingDong = data
                        MHConfig.ddSteps = steps
                    }
                }
                ToastUtils.showLong("保存成功")
            } else {
                ToastUtils.showLong("JSON 数据非法")
            }
        }

        loadData()

    }

    fun loadData() {
        when (MHData.buyPlatform) {
            1 -> {
                binding.helpSteps.setText(JSONUtil.formatJsonStr(MHData.stepsJsonMeiTuan))
            }
            else -> {
                binding.helpSteps.setText(JSONUtil.formatJsonStr(MHData.stepsJsonDingDong))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}