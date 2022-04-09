package com.univerindream.maicaiassistant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.hutool.json.JSONUtil
import com.blankj.utilcode.util.ToastUtils
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
            MHData.jsonSteps = ""

            loadData()
        }
        binding.helpSave.setOnClickListener {
            val data = binding.helpSteps.text.toString()

            if (JSONUtil.isTypeJSONArray(data)) {
                MHData.jsonSteps = data
                ToastUtils.showLong("保存成功")
            } else {
                ToastUtils.showLong("JSON 数据非法")
            }
        }

        loadData()

    }

    fun loadData() {
        binding.helpSteps.setText(JSONUtil.formatJsonStr(MHData.jsonSteps))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}