package com.univerindream.maicaiassistant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.elvishew.xlog.XLog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import com.univerindream.maicaiassistant.MCCond
import com.univerindream.maicaiassistant.MCHandle
import com.univerindream.maicaiassistant.MCStep
import com.univerindream.maicaiassistant.R
import com.univerindream.maicaiassistant.databinding.FragmentStepBinding
import com.univerindream.maicaiassistant.databinding.ItemCondBinding

class StepFragment : Fragment() {
    private var _binding: FragmentStepBinding? = null

    private val binding get() = _binding!!

    private val args: StepFragmentArgs by navArgs()

    private val itemAdapter by lazy {
        ItemAdapter<BindingCondItem>()
    }

    private val fastAdapter by lazy {
        FastAdapter.with(itemAdapter)
    }

    private val mStep: MCStep by lazy {
        GsonUtils.fromJson(args.stepJson, MCStep::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("updateHandle") { requestKey, bundle ->
            XLog.i("handle -> $requestKey $bundle")
            mStep.handle = GsonUtils.fromJson(bundle.getString("handleJson"), MCHandle::class.java)
        }

        setFragmentResultListener("updateCond") { requestKey, bundle ->
            XLog.i("handle -> $requestKey $bundle")
            val mcCond = GsonUtils.fromJson(bundle.getString("condJson"), MCCond::class.java)
            val index = bundle.getInt("condIndex")

            if (index == -1) {
                mStep.condList.add(mcCond)
            } else {
                mStep.condList[index] = mcCond
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.stepCondValue.adapter = fastAdapter
        binding.stepCondValue.layoutManager = LinearLayoutManager(requireContext())
        binding.stepHandleCard.setOnClickListener {
            findNavController().navigate(
                StepFragmentDirections.actionStepFragmentToHandleFragment(
                    GsonUtils.toJson(
                        mStep.handle
                    )
                )
            )
        }
        binding.stepAlarm.setOnCheckedChangeListener { compoundButton, b ->
            if (!compoundButton.isPressed) return@setOnCheckedChangeListener
            mStep.isAlarm = b
        }
        binding.stepManual.setOnCheckedChangeListener { compoundButton, b ->
            if (!compoundButton.isPressed) return@setOnCheckedChangeListener
            mStep.isManual = b
        }
        binding.stepRepeat.setOnCheckedChangeListener { compoundButton, b ->
            if (!compoundButton.isPressed) return@setOnCheckedChangeListener
            mStep.isRepeat = b
        }
        binding.stepFailBack.setOnCheckedChangeListener { compoundButton, b ->
            if (!compoundButton.isPressed) return@setOnCheckedChangeListener
            mStep.isFailBack = b
        }

        binding.floatingAddButton.setOnClickListener {
            val action = StepFragmentDirections.actionStepFragmentToCondFragment(
                condIndex = -1,
                condJson = "{}"
            )
            findNavController().navigate(action)
        }
        binding.floatingSaveButton.setOnClickListener {
            saveData()
        }

        fastAdapter.onClickListener = { _, _, _, position ->
            val action = StepFragmentDirections.actionStepFragmentToCondFragment(
                condIndex = position,
                condJson = GsonUtils.toJson(mStep.condList[position])
            )
            findNavController().navigate(action)
            false
        }
        fastAdapter.onLongClickListener = { _, _, _, position ->
            MaterialAlertDialogBuilder(requireContext())
                .setMessage("删除该条件？")
                .setNegativeButton("取消") { dialog, which ->
                    // Respond to negative button press
                    dialog.cancel()
                }
                .setPositiveButton("确定") { dialog, which ->
                    // Respond to positive button press
                    mStep.condList.removeAt(position)
                    loadData()
                }
                .show()
            false
        }

        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    fun loadData() {
        val step: MCStep = mStep
        binding.stepName.text = step.name

        binding.stepAlarm.isChecked = step.isAlarm
        binding.stepManual.isChecked = step.isManual
        binding.stepRepeat.isChecked = step.isRepeat
        binding.stepFailBack.isChecked = step.isFailBack

        var handleContent = "类型：${step.handle.type}"
        handleContent += "\n节点："
        handleContent += "\n   - 类型：${step.handle.node.nodeType}"
        handleContent += "\n   - key：${step.handle.node.nodeKey}"
        handleContent += "\n   - packageName：${step.handle.node.packageName}"
        handleContent += "\n   - className：${step.handle.node.className}"
        handleContent += "\n执行前延迟(ms)：${step.handle.delayRunBefore}"
        handleContent += "\n执行后延迟(ms)：${step.handle.delayRunAfter}"
        binding.stepHandleValue.text = handleContent

        itemAdapter.clear()
        step.condList.forEachIndexed { index, mcStep ->
            itemAdapter.add(BindingCondItem(mcStep).apply {
                tag = index + 1
            })
        }
    }

    fun saveData() {


        val stepJson = GsonUtils.toJson(mStep)

        setFragmentResult("updateStep", bundleOf("stepJson" to stepJson, "stepIndex" to args.stepIndex))
        findNavController().navigateUp()
    }

    class BindingCondItem(model: MCCond) : ModelAbstractBindingItem<MCCond, ItemCondBinding>(model) {

        override val type: Int
            get() = R.id.adapter_cond_item

        override fun bindView(binding: ItemCondBinding, payloads: List<Any>) {
            binding.adapterCondNo.text = "条件" + tag.toString()

            var condContent = "类型：${model.type}"
            condContent += "\n节点："
            condContent += "\n   - 类型：${model.node.nodeType}"
            condContent += "\n   - key：${model.node.nodeKey}"
            condContent += "\n   - packageName：${model.node.packageName}"
            condContent += "\n   - className：${model.node.className}"
            binding.adapterCondValue.text = condContent

        }

        override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemCondBinding {
            return ItemCondBinding.inflate(inflater, parent, false)
        }
    }

}