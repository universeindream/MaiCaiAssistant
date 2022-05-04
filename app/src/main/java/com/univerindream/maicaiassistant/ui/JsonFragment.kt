package com.univerindream.maicaiassistant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.JsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.univerindream.maicaiassistant.MCFile
import com.univerindream.maicaiassistant.MCUtil
import com.univerindream.maicaiassistant.data.DataRepository
import com.univerindream.maicaiassistant.databinding.FragmentJsonBinding
import com.univerindream.maicaiassistant.model.MCSolution
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JsonFragment : Fragment() {

    @Inject
    lateinit var dataRepository: DataRepository

    private var _binding: FragmentJsonBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args: JsonFragmentArgs by navArgs()

    private val mSolution: MCSolution by lazy {
        if (args.solutionId.isBlank()) dataRepository.curSolution else MCFile.getSolution(args.solutionId)
    }

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
                val solution = GsonUtils.fromJson(data, MCSolution::class.java)
                if (!MCUtil.isValid(solution)) {
                    ToastUtils.showLong("该方案当前版本不支持")
                    return@setOnClickListener
                }

                if (solution.id == dataRepository.curSolution.id) {
                    dataRepository.curSolution = solution
                } else {
                    MCFile.saveSolution(solution)
                }
                ToastUtils.showLong("保存成功")
            } catch (e: Exception) {
                ToastUtils.showLong("JSON 数据非法")
            }
        }
        binding.jsonExport.setOnClickListener {
            val data = binding.jsonContent.text.toString()
            ClipboardUtils.copyText(JsonUtils.formatJson(data, 0))
            ToastUtils.showLong("已将内容复制到剪切板，可以分享给别人啦")
        }
        binding.jsonImport.setOnClickListener {
            val content = ClipboardUtils.getText()
            if (content.isNullOrBlank()) {
                ToastUtils.showLong("未读取到剪切板内容")
            } else {
                binding.jsonContent.setText(JsonUtils.formatJson(content.toString()))
                ToastUtils.showLong("已将剪切板内容复制到编辑框，记得保存")
            }
        }

        loadData()

    }

    fun loadData() {
        val json = GsonUtils.toJson(mSolution)
        binding.jsonContent.setText(JsonUtils.formatJson(json))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}