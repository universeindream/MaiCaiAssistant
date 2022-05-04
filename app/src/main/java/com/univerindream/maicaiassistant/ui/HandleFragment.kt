package com.univerindream.maicaiassistant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.GsonUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.univerindream.maicaiassistant.R
import com.univerindream.maicaiassistant.databinding.FragmentHandleBinding
import com.univerindream.maicaiassistant.model.EHandle
import com.univerindream.maicaiassistant.model.MCHandle

class HandleFragment : Fragment() {
    private var _binding: FragmentHandleBinding? = null

    private val binding get() = _binding!!

    private val args: HandleFragmentArgs by navArgs()

    private val mHandle: MCHandle by lazy {
        GsonUtils.fromJson(args.handleJson, MCHandle::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHandleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.handleTypeSelect.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.list_popup_window_item,
                EHandle.values().map { it.toStr() }.toList()
            )
        )
        binding.handleType.editText?.doAfterTextChanged { inputText ->
            mHandle.type = EHandle.strOf(inputText?.toString())
        }

        binding.handleDelayBefore.editText?.doAfterTextChanged { inputText ->
            mHandle.delayRunBefore = inputText?.toString()?.toLongOrNull() ?: 1000L
        }

        binding.handleDelay.editText?.doAfterTextChanged { inputText ->
            mHandle.delayRunAfter = inputText?.toString()?.toLongOrNull() ?: 1000L
        }

        binding.nodePackage.editText?.doAfterTextChanged { inputText ->
            mHandle.node.packageName = inputText?.toString() ?: ""
        }

        binding.nodePackage.editText?.doAfterTextChanged { inputText ->
            mHandle.node.packageName = inputText?.toString() ?: ""
        }

        binding.nodeClass.editText?.doAfterTextChanged { inputText ->
            mHandle.node.className = inputText?.toString() ?: ""
        }

        binding.nodeId.editText?.doAfterTextChanged { inputText ->
            mHandle.node.id = inputText?.toString() ?: ""
        }

        binding.nodeTxt.editText?.doAfterTextChanged { inputText ->
            mHandle.node.txt = inputText?.toString() ?: ""
        }

        binding.nodeCoordinate.editText?.doAfterTextChanged { inputText ->
            mHandle.node.coordinate = inputText?.toString() ?: ""
        }

        binding.nodeIndex.editText?.doAfterTextChanged { inputText ->
            mHandle.node.index = (inputText?.toString()?.toIntOrNull() ?: 1) - 1
        }

        binding.nodePackageSelect.setOnClickListener {
            val apps = AppUtils.getAppsInfo()
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("获取应用包名")
                .setItems(apps.map { it.name }.toTypedArray()) { _, which ->
                    binding.nodePackage.editText?.setText(apps[which].packageName)
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

        binding.handleTypeSelect.setText(mcHandle.type.toStr(), false)
        binding.handleDelay.editText?.setText(mcHandle.delayRunAfter.toString())
        binding.handleDelayBefore.editText?.setText(mcHandle.delayRunBefore.toString())
        binding.nodePackage.editText?.setText(mcHandle.node.packageName)
        binding.nodeClass.editText?.setText(mcHandle.node.className)
        binding.nodeId.editText?.setText(mcHandle.node.id)
        binding.nodeTxt.editText?.setText(mcHandle.node.txt)
        binding.nodeCoordinate.editText?.setText(mcHandle.node.coordinate)
        val index = "${mcHandle.node.index + 1}"
        binding.nodeIndex.editText?.setText(index)

    }

    fun saveData() {
        setFragmentResult("updateHandle", bundleOf("handleJson" to GsonUtils.toJson(mHandle)))
        findNavController().navigateUp()
    }
}