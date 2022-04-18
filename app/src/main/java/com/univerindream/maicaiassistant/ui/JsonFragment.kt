package com.univerindream.maicaiassistant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.JsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.univerindream.maicaiassistant.MCSolution
import com.univerindream.maicaiassistant.MHConfig
import com.univerindream.maicaiassistant.MHData
import com.univerindream.maicaiassistant.databinding.FragmentJsonBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class JsonFragment : Fragment() {

    private var _binding: FragmentJsonBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentJsonBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.jsonSave.setOnClickListener {
            val data = binding.jsonContent.text.toString()

            try {
                GsonUtils.fromJson(data, MCSolution::class.java)
                MHData.curMCSolutionJSON = data
                ToastUtils.showLong("保存成功")
            } catch (e: Exception) {
                ToastUtils.showLong("JSON 数据非法")
            }
        }
        binding.jsonExport.setOnClickListener {
            ClipboardUtils.copyText(GsonUtils.toJson(MHConfig.curMCSolution))
            ToastUtils.showLong("已将当前方案导出至剪切板，可以复制给别人啦")
        }
        binding.jsonImport.setOnClickListener {
            val content = ClipboardUtils.getText()
            if (content.isNullOrBlank()) {
                ToastUtils.showLong("未读取到剪切板内容")
            } else {
                binding.jsonContent.setText(
                    JsonUtils.formatJson(content.toString())
                )
                ToastUtils.showLong("已将剪切板内容导入至编辑框")
            }

        }

        loadData()

    }

    fun loadData() {
        val json = GsonUtils.toJson(MHConfig.curMCSolution)
        binding.jsonContent.setText(JsonUtils.formatJson(json))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}