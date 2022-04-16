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
            mStep.condList[index] = mcCond
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
        binding.floatingActionButton.setOnClickListener {
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

        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    fun loadData() {
        val step: MCStep = mStep
        binding.stepName.text = step.name

        var handleContent = "类型：${step.handle.type}"
        handleContent += "\n节点："
        handleContent += "\n   - 类型：${step.handle.node.nodeType}"
        handleContent += "\n   - key：${step.handle.node.nodeKey}"
        handleContent += "\n   - packageName：${step.handle.node.packageName}"
        handleContent += "\n   - className：${step.handle.node.className}"
        handleContent += "\n执行后延迟(ms)：${step.handle.delay}"
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