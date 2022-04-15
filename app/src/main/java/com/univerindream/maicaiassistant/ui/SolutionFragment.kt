package com.univerindream.maicaiassistant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolutionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.solutionSteps.adapter = fastAdapter
        binding.solutionSteps.layoutManager = LinearLayoutManager(requireContext())

        fastAdapter.onClickListener = { _, _, _, position ->
            val action = SolutionFragmentDirections.actionSolutionFragmentToStepFragment(stepIndex = position)
            findNavController().navigate(action)
            false
        }

        loadData()
    }

    fun loadData() {
        binding.solutionNameValue.text = MHConfig.curMHSolution.name
        itemAdapter.clear()
        MHConfig.curMHSolution.steps.forEachIndexed { index, mcStep ->
            itemAdapter.add(BindingStepItem(mcStep).apply {
                tag = index + 1
            })
        }
    }

    class BindingStepItem(model: MCStep) : ModelAbstractBindingItem<MCStep, ItemStepBinding>(model) {

        override val type: Int
            get() = R.id.adapter_step_item

        override fun bindView(binding: ItemStepBinding, payloads: List<Any>) {
            val content = "Step$tag: ${model.name}"
            binding.solutionStepName.text = content
        }

        override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemStepBinding {
            return ItemStepBinding.inflate(inflater, parent, false)
        }
    }

}