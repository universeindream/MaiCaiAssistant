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
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.GsonUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.univerindream.maicaiassistant.*
import com.univerindream.maicaiassistant.databinding.FragmentHandleBinding

class HandleFragment : Fragment() {
    private var _binding: FragmentHandleBinding? = null

    private val binding get() = _binding!!


    private val args: HandleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHandleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.fragmentHandleType.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.list_popup_window_item,
                EMCHandle.values().map { it.toStr() }.toList()
            )
        )

        binding.fragmentHandleNodeType.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.list_popup_window_item,
                EMCNodeType.values().map { it.toStr() }.toList()
            )
        )

        binding.fragmentHandleSelectPackageName.setOnClickListener {
            val apps = AppUtils.getAppsInfo().filter { !it.isSystem }
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("获取应用包名")
                .setItems(apps.map { it.name }.toTypedArray()) { _, which ->
                    binding.fragmentHandleNodePackageName.editText?.setText(apps[which].packageName)
                }
                .show()
        }

        binding.floatingActionButton.setOnClickListener {
            saveData()
        }

        loadData()
    }

    fun loadData() {
        val mcHandle = GsonUtils.fromJson(args.handleJson, MCHandle::class.java)

        binding.fragmentHandleType.setText(mcHandle.type.toStr(), false)
        binding.fragmentHandleDelay.editText?.setText(mcHandle.delayRunAfter.toString())
        binding.fragmentHandleDelayBefore.editText?.setText(mcHandle.delayRunBefore.toString())
        binding.fragmentHandleNodeType.setText(mcHandle.node.nodeType.toStr(), false)
        binding.fragmentHandleNodeKey.editText?.setText(mcHandle.node.nodeKey)
        binding.fragmentHandleNodeIndex.editText?.setText(mcHandle.node.nodeIndex.toString())
        binding.fragmentHandleNodePackageName.editText?.setText(mcHandle.node.packageName)
        binding.fragmentHandleNodeClassName.editText?.setText(mcHandle.node.className)

    }

    fun saveData() {
        val type = EMCHandle.strOf(binding.fragmentHandleType.text.toString())

        var delay = binding.fragmentHandleDelay.editText?.text?.toString()
        if (delay.isNullOrBlank()) delay = "0"

        var delayBefore = binding.fragmentHandleDelayBefore.editText?.text?.toString()
        if (delayBefore.isNullOrBlank()) delayBefore = "0"

        val nodeType = EMCNodeType.strOf(binding.fragmentHandleNodeType.text.toString())
        val nodeKey = binding.fragmentHandleNodeKey.editText?.text.toString()

        var nodeIndex = binding.fragmentHandleNodeIndex.editText?.text?.toString()
        if (nodeIndex.isNullOrBlank()) nodeIndex = "0"

        val nodePackageName = binding.fragmentHandleNodePackageName.editText?.text.toString()
        val nodeClassName = binding.fragmentHandleNodeClassName.editText?.text.toString()

        val handleJson = GsonUtils.toJson(
            MCHandle(
                type = type,
                delayRunAfter = delay.toLong(),
                delayRunBefore = delayBefore.toLong(),
                node = MCNode(
                    nodeType = nodeType,
                    nodeKey = nodeKey,
                    nodeIndex = nodeIndex.toInt(),
                    className = nodeClassName,
                    packageName = nodePackageName
                )
            )
        )

        setFragmentResult("updateHandle", bundleOf("handleJson" to handleJson))
        findNavController().navigateUp()
    }
}