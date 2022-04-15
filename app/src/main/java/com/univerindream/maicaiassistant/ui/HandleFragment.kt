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
import com.univerindream.maicaiassistant.EMCHandle
import com.univerindream.maicaiassistant.EMCSearch
import com.univerindream.maicaiassistant.MCHandle
import com.univerindream.maicaiassistant.R
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
                EMCHandle.values().map { it.toString() }.toList()
            )
        )

        binding.fragmentHandleNodeType.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.list_popup_window_item,
                EMCSearch.values().map { it.toString() }.toList()
            )
        )

        binding.floatingActionButton.setOnClickListener {
            setFragmentResult("updateHandle", bundleOf("kim" to "test"))
            findNavController().navigateUp()
        }

        loadData()
    }

    fun loadData() {
        val mcHandle = GsonUtils.fromJson(args.handleJson, MCHandle::class.java)

        binding.fragmentHandleType.setText(mcHandle.type.toString(), false)
        binding.fragmentHandleDelay.editText?.setText(mcHandle.delay.toString())
        binding.fragmentHandleNodeType.setText(mcHandle.node.search.toString(), false)
        binding.fragmentHandleNodeKey.editText?.setText(mcHandle.node.nodeKey)
        binding.fragmentHandleNodePackageName.editText?.setText(mcHandle.node.packageName)
        binding.fragmentHandleNodeClassName.editText?.setText(mcHandle.node.className)

    }

}