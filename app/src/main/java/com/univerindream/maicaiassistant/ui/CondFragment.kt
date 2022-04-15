package com.univerindream.maicaiassistant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.blankj.utilcode.util.GsonUtils
import com.univerindream.maicaiassistant.EMCCond
import com.univerindream.maicaiassistant.EMCSearch
import com.univerindream.maicaiassistant.MCCond
import com.univerindream.maicaiassistant.R
import com.univerindream.maicaiassistant.databinding.FragmentCondBinding

class CondFragment : Fragment() {
    private var _binding: FragmentCondBinding? = null

    private val binding get() = _binding!!


    private val args: CondFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.fragmentCondType.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.list_popup_window_item,
                EMCCond.values().map { it.toString() }.toList()
            )
        )

        binding.fragmentCondNodeType.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.list_popup_window_item,
                EMCSearch.values().map { it.toString() }.toList()
            )
        )
        binding.floatingActionButton.setOnClickListener {
            setFragmentResult("updateCond", bundleOf("kim" to "test"))
            findNavController().navigateUp()
        }

        loadData()
    }

    fun loadData() {
        val cond = GsonUtils.fromJson(args.condJson, MCCond::class.java)

        binding.fragmentCondType.setText(cond.type.toString(), false)
        binding.fragmentCondNodeType.setText(cond.node.search.toString(), false)
        binding.fragmentCondNodeKey.editText?.setText(cond.node.nodeKey)
        binding.fragmentCondNodePackageName.editText?.setText(cond.node.packageName)
        binding.fragmentCondNodeClassName.editText?.setText(cond.node.className)

    }

}