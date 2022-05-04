package com.univerindream.maicaiassistant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import com.univerindream.maicaiassistant.BuildConfig
import com.univerindream.maicaiassistant.R
import com.univerindream.maicaiassistant.databinding.FragmentSolutionBinding
import com.univerindream.maicaiassistant.databinding.ItemStepBinding
import com.univerindream.maicaiassistant.model.MCSolution
import com.univerindream.maicaiassistant.model.MCStep
import com.univerindream.maicaiassistant.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SolutionFragment : Fragment() {

    private val sharedModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentSolutionBinding? = null
    private val binding get() = _binding!!

    private val args: SolutionFragmentArgs by navArgs()
    private val mSolution: MCSolution by lazy {
        sharedModel.getSolution(args.solutionId)
    }

    private val itemAdapter by lazy {
        ItemAdapter<BindingStepItem>()
    }

    private val fastAdapter by lazy {
        FastAdapter.with(itemAdapter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("updateStep") { requestKey, bundle ->
            LogUtils.i("handle -> $requestKey $bundle")
            val mcStep = GsonUtils.fromJson(bundle.getString("stepJson"), MCStep::class.java)
            val index = bundle.getInt("stepIndex")
            if (index == -1) {
                mSolution.steps.add(mcStep)
            } else {
                mSolution.steps[index] = mcStep
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolutionBinding.inflate(inflater, container, false)

        binding.solutionName.editText?.doAfterTextChanged { inputText ->
            mSolution.name = inputText?.toString()?.ifBlank { "" } ?: ""
        }
        binding.solutionAuthor.editText?.doAfterTextChanged { inputText ->
            mSolution.author = inputText?.toString()?.ifBlank { "" } ?: ""
        }
        binding.solutionDesc.editText?.doAfterTextChanged { inputText ->
            mSolution.desc = inputText?.toString()?.ifBlank { "" } ?: ""
        }
        binding.solutionLaunchTime.editText?.doAfterTextChanged { inputText ->
            mSolution.launchTime = (inputText?.toString()?.ifBlank { "25" } ?: "25").toLong()
        }
        binding.solutionDelay.editText?.doAfterTextChanged { inputText ->
            mSolution.stepDelay = (inputText?.toString()?.ifBlank { "100" } ?: "100").toLong()
        }
        binding.solutionFailAlarm.setOnCheckedChangeListener { compoundButton, b ->
            if (!compoundButton.isPressed) return@setOnCheckedChangeListener
            mSolution.failIsAlarm = b
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

        binding.solutionSteps.adapter = fastAdapter
        binding.solutionSteps.layoutManager = LinearLayoutManager(requireContext())
        fastAdapter.onClickListener = { _, _, _, position ->
            val stepJson = GsonUtils.toJson(mSolution.steps[position])
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
                                val step = mSolution.steps[position]
                                mSolution.steps.removeAt(position)
                                mSolution.steps.add(position - 1, step)
                                loadData()
                            }
                        }
                        1 -> {
                            if (position == mSolution.steps.size - 1) {
                                ToastUtils.showShort("已经是最后一步了")
                            } else {
                                val step = mSolution.steps[position]
                                mSolution.steps.removeAt(position)
                                mSolution.steps.add(position + 1, step)
                                loadData()
                            }
                        }
                        2 -> {
                            mSolution.steps.removeAt(position)
                            loadData()
                        }
                    }
                    dialog.dismiss()
                }
                .show()
            false
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    fun loadData() {
        binding.solutionName.editText?.setText(mSolution.name)
        binding.solutionAuthor.editText?.setText(mSolution.author)
        binding.solutionDesc.editText?.setText(mSolution.desc)
        binding.solutionDelay.editText?.setText(mSolution.stepDelay.toString())
        binding.solutionLaunchTime.editText?.setText(mSolution.launchTime.toString())
        binding.solutionFailAlarm.isChecked = mSolution.failIsAlarm
        itemAdapter.clear()
        mSolution.steps.forEachIndexed { index, mcStep ->
            itemAdapter.add(BindingStepItem(mcStep).apply {
                tag = index + 1
            })
        }
    }

    fun saveData() {
        lifecycleScope.launch {
            mSolution.appVersion = BuildConfig.VERSION_NAME
            mSolution.updateDateStr = TimeUtils.getNowString()
            sharedModel.saveSolution(mSolution)

            findNavController().navigateUp()
        }
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