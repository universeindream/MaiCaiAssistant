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
import com.univerindream.maicaiassistant.*
import com.univerindream.maicaiassistant.databinding.FragmentCondBinding

class CondFragment : Fragment() {
    private var _binding: FragmentCondBinding? = null

    private val binding get() = _binding!!

    private val args: CondFragmentArgs by navArgs()

    private val mcCond: MCCond by lazy {
        GsonUtils.fromJson(args.condJson, MCCond::class.java)
    }

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
                EMCNodeType.values().map { it.toString() }.toList()
            )
        )
        binding.floatingActionButton.setOnClickListener {

            saveData()
        }

        loadData()
    }

    fun loadData() {
        val cond = mcCond

        binding.fragmentCondType.setText(cond.type.toString(), false)
        binding.fragmentCondNodeType.setText(cond.node.nodeType.toString(), false)
        binding.fragmentCondNodeKey.editText?.setText(cond.node.nodeKey)
        binding.fragmentCondNodeIndex.editText?.setText(cond.node.nodeIndex.toString())
        binding.fragmentCondNodePackageName.editText?.setText(cond.node.packageName)
        binding.fragmentCondNodeClassName.editText?.setText(cond.node.className)

    }

    fun saveData() {

        val type = EMCCond.valueOf(binding.fragmentCondType.text.toString())
        val nodeType = EMCNodeType.valueOf(binding.fragmentCondNodeType.text.toString())
        val nodeKey = binding.fragmentCondNodeKey.editText?.text.toString()
        val nodeIndex = binding.fragmentCondNodeIndex.editText?.text?.toString()?.toInt() ?: 0
        val nodePackageName = binding.fragmentCondNodePackageName.editText?.text.toString()
        val nodeClassName = binding.fragmentCondNodeClassName.editText?.text.toString()

        val condJson = GsonUtils.toJson(
            MCCond(
                type = type,
                node = MCNode(
                    nodeType = nodeType,
                    nodeKey = nodeKey,
                    nodeIndex = nodeIndex,
                    className = nodeClassName,
                    packageName = nodePackageName
                )
            )
        )


        setFragmentResult("updateCond", bundleOf("condJson" to condJson, "condIndex" to args.condIndex))
        findNavController().navigateUp()
    }

}