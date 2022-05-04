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
import com.univerindream.maicaiassistant.databinding.FragmentCondBinding
import com.univerindream.maicaiassistant.model.ECond
import com.univerindream.maicaiassistant.model.MCCond

class CondFragment : Fragment() {
    private var _binding: FragmentCondBinding? = null

    private val binding get() = _binding!!

    private val args: CondFragmentArgs by navArgs()

    private val mCond: MCCond by lazy {
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



        binding.condTypeSelect.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.list_popup_window_item,
                ECond.values().map { it.toStr() }.toList()
            )
        )
        binding.condType.editText?.doAfterTextChanged { inputText ->
            mCond.type = ECond.strOf(inputText?.toString())
        }

        binding.nodePackage.editText?.doAfterTextChanged { inputText ->
            mCond.node.packageName = inputText?.toString() ?: ""
        }

        binding.nodePackage.editText?.doAfterTextChanged { inputText ->
            mCond.node.packageName = inputText?.toString() ?: ""
        }

        binding.nodeClass.editText?.doAfterTextChanged { inputText ->
            mCond.node.className = inputText?.toString() ?: ""
        }

        binding.nodeId.editText?.doAfterTextChanged { inputText ->
            mCond.node.id = inputText?.toString() ?: ""
        }

        binding.nodeTxt.editText?.doAfterTextChanged { inputText ->
            mCond.node.txt = inputText?.toString() ?: ""
        }

        binding.nodeIndex.editText?.doAfterTextChanged { inputText ->
            mCond.node.index = (inputText?.toString()?.toIntOrNull() ?: 1) - 1
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

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.list_popup_window_item, arrayListOf("", "是", "否"))
        binding.nodeIsEnabledAuto.setAdapter(arrayAdapter)
        binding.nodeIsEnabled.editText?.doAfterTextChanged { inputText ->
            mCond.node.isEnabled = when (inputText?.toString()) {
                "是" -> true
                "否" -> false
                else -> null
            }
        }
        binding.nodeVisibleAuto.setAdapter(arrayAdapter)
        binding.nodeVisible.editText?.doAfterTextChanged { inputText ->
            mCond.node.isVisibleToUser = when (inputText?.toString()) {
                "是" -> true
                "否" -> false
                else -> null
            }
        }
        binding.nodeIsCheckedAuto.setAdapter(arrayAdapter)
        binding.nodeIsChecked.editText?.doAfterTextChanged { inputText ->
            mCond.node.isChecked = when (inputText?.toString()) {
                "是" -> true
                "否" -> false
                else -> null
            }
        }
        binding.nodeIsSelectedAuto.setAdapter(arrayAdapter)
        binding.nodeIsSelected.editText?.doAfterTextChanged { inputText ->
            mCond.node.isSelected = when (inputText?.toString()) {
                "是" -> true
                "否" -> false
                else -> null
            }
        }
        binding.nodeIsClickableAuto.setAdapter(arrayAdapter)
        binding.nodeIsClickable.editText?.doAfterTextChanged { inputText ->
            mCond.node.isClickable = when (inputText?.toString()) {
                "是" -> true
                "否" -> false
                else -> null
            }
        }

        binding.floatingActionButton.setOnClickListener {

            saveData()
        }

        loadData()
    }

    fun loadData() {
        binding.condTypeSelect.setText(mCond.type.toStr(), false)
        binding.nodePackage.editText?.setText(mCond.node.packageName)
        binding.nodeClass.editText?.setText(mCond.node.className)
        binding.nodeId.editText?.setText(mCond.node.id)
        binding.nodeTxt.editText?.setText(mCond.node.txt)
        val index = "${mCond.node.index + 1}"
        binding.nodeIndex.editText?.setText(index)

        binding.nodeIsEnabled.editText?.setText(itemStatus(mCond.node.isEnabled))
        binding.nodeVisible.editText?.setText(itemStatus(mCond.node.isVisibleToUser))
        binding.nodeIsChecked.editText?.setText(itemStatus(mCond.node.isChecked))
        binding.nodeIsSelected.editText?.setText(itemStatus(mCond.node.isSelected))
        binding.nodeIsClickable.editText?.setText(itemStatus(mCond.node.isClickable))
    }

    private fun itemStatus(status: Boolean?) = when (status) {
        true -> "是"
        false -> "否"
        else -> ""
    }

    fun saveData() {
        setFragmentResult("updateCond", bundleOf("condJson" to GsonUtils.toJson(mCond), "condIndex" to args.condIndex))
        findNavController().navigateUp()
    }

}