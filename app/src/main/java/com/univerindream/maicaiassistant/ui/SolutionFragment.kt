package com.univerindream.maicaiassistant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.elvishew.xlog.XLog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import com.univerindream.maicaiassistant.MCStep
import com.univerindream.maicaiassistant.MHConfig
import com.univerindream.maicaiassistant.R
import com.univerindream.maicaiassistant.databinding.FragmentSolutionBinding
import com.univerindream.maicaiassistant.databinding.ItemStepBinding

class SolutionFragment : Fragment() {
    private var _binding: FragmentSolutionBinding? = null

    private val binding get() = _binding!!

    private val itemAdapter by lazy {
        ItemAdapter<BindingStepItem>()
    }

    private val fastAdapter by lazy {
        FastAdapter.with(itemAdapter)
    }

    private val mcSolution by lazy {
        MHConfig.curMCSolution
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("updateStep") { requestKey, bundle ->
            XLog.i("handle -> $requestKey $bundle")
            val mcStep = GsonUtils.fromJson(bundle.getString("stepJson"), MCStep::class.java)
            val index = bundle.getInt("stepIndex")
            if (index == -1) {
                mcSolution.steps.add(mcStep)
            } else {
                mcSolution.steps[index] = mcStep
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolutionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.solutionName.editText?.doAfterTextChanged { inputText ->
            val name = if (inputText.isNullOrBlank()) "" else inputText.toString()
            mcSolution.name = name
        }
        binding.solutionSteps.adapter = fastAdapter
        binding.solutionSteps.layoutManager = LinearLayoutManager(requireContext())
        fastAdapter.onClickListener = { _, _, _, position ->
            val stepJson = GsonUtils.toJson(mcSolution.steps[position])
            val action = SolutionFragmentDirections.actionSolutionFragmentToStepFragment(
                stepJson = stepJson,
                stepIndex = position
            )
            findNavController().navigate(action)
            false
        }
        fastAdapter.onLongClickListener = { _, _, _, position ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("操作")
                .setItems(arrayOf("上移", "下移", "删除")) { dialog, which ->
                    // Respond to item chosen
                    when (which) {
                        0 -> {
                            if (position == 0) {
                                ToastUtils.showShort("已经是第一步了")
                            } else {
                                val step = mcSolution.steps[position]
                                mcSolution.steps.removeAt(position)
                                mcSolution.steps.add(position - 1, step)
                                loadData()
                            }
                        }
                        1 -> {
                            if (position == mcSolution.steps.size - 1) {
                                ToastUtils.showShort("已经是最后一步了")
                            } else {
                                val step = mcSolution.steps[position]
                                mcSolution.steps.removeAt(position)
                                mcSolution.steps.add(position + 1, step)
                                loadData()
                            }
                        }
                        2 -> {
                            mcSolution.steps.removeAt(position)
                            loadData()
                        }
                    }
                    dialog.dismiss()
                }
                .show()
            false
        }

        binding.floatingSaveButton.setOnClickListener {
            saveData()
        }
        binding.floatingAddButton.setOnClickListener {
            val action = SolutionFragmentDirections.actionSolutionFragmentToStepFragment(
                stepJson = "{}",
                stepIndex = -1
            )
            findNavController().navigate(action)
        }

        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    fun loadData() {
        binding.solutionName.editText?.setText(mcSolution.name)
        itemAdapter.clear()
        mcSolution.steps.forEachIndexed { index, mcStep ->
            itemAdapter.add(BindingStepItem(mcStep).apply {
                tag = index + 1
            })
        }
    }

    fun saveData() {
        MHConfig.curMCSolution = mcSolution
        findNavController().navigateUp()
    }

    class BindingStepItem(model: MCStep) : ModelAbstractBindingItem<MCStep, ItemStepBinding>(model) {

        override val type: Int
            get() = R.id.adapter_step_item

        override fun bindView(binding: ItemStepBinding, payloads: List<Any>) {
            val content = "步骤$tag: ${model.name}"
            binding.solutionStepName.text = content
            val condContent = "条件:${model.condList.size}个"
            binding.solutionStepCondList.text = condContent
            val handleContent = "操作:${model.handle.type.toStr()}"
            binding.solutionStepHandle.text = handleContent

            if (model.isEnable) {
                binding.solutionStepName.setTextColor(ColorUtils.getColor(android.R.color.holo_green_dark))
            } else {
                binding.solutionStepName.setTextColor(ColorUtils.getColor(android.R.color.holo_red_dark))
            }
        }

        override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemStepBinding {
            return ItemStepBinding.inflate(inflater, parent, false)
        }
    }

}