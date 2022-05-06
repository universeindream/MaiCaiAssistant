package com.univerindream.maicaiassistant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.univerindream.maicaiassistant.MCUtil
import com.univerindream.maicaiassistant.adapter.LocalSolutionItem
import com.univerindream.maicaiassistant.databinding.FragmentDiscoverBinding
import com.univerindream.maicaiassistant.model.MCSolution
import com.univerindream.maicaiassistant.viewmodels.DiscoverViewModel
import com.univerindream.maicaiassistant.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class DiscoverFragment : Fragment() {

    private val sharedModel: SharedViewModel by activityViewModels()
    private val viewModel: DiscoverViewModel by viewModels()

    private var apiJob: Job? = null

    private val itemAdapter: ItemAdapter<LocalSolutionItem> by lazy {
        ItemAdapter()
    }
    private val fastAdapter by lazy {
        FastAdapter.with(itemAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDiscoverBinding.inflate(inflater, container, false)

        binding.rvSolutions.adapter = fastAdapter
        binding.rvSolutions.layoutManager = LinearLayoutManager(requireContext())
        fastAdapter.onClickListener = { view, _, item, position ->
            val items = arrayOf("保存")

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("操作")
                .setItems(items) { dialog, which ->
                    when (items[which]) {
                        "保存" -> {
                            addOrReplaceSolution(item.model)
                        }
                    }
                    dialog.dismiss()
                }
                .show()
            false
        }

        viewModel.showProgress.observe(viewLifecycleOwner) {
            binding.progress.isVisible = it
        }

        getPublicSolutions()

        return binding.root
    }

    private fun getPublicSolutions() {
        apiJob?.cancel()
        apiJob = lifecycleScope.launch {
            viewModel.showProgress.value = true
            viewModel.getPublicSolutions().collectLatest {
                viewModel.showProgress.value = false
                if (it.isSuccess) {
                    val solutions = it.getOrNull() ?: return@collectLatest
                    itemAdapter.clear()
                    itemAdapter.add(solutions.map { LocalSolutionItem(it) })
                } else {
                    ToastUtils.showLong("请求失败")
                }
            }
        }
    }


    private fun addOrReplaceSolution(solution: MCSolution) {
        if (!MCUtil.isValid(solution)) {
            ToastUtils.showLong("该方案当前版本不支持")
            return
        }

        if (sharedModel.isExistSolution(solution.id)) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("提示")
                .setMessage("该方案${MCUtil.getUUID8(solution.id)},本地已存在")
                .setNeutralButton("取消") { dialog, _ ->
                    dialog.cancel()
                }
                .setNegativeButton("覆盖") { _, _ ->
                    sharedModel.saveSolution(solution)
                    ToastUtils.showLong("方案覆盖成功")
                }
                .setPositiveButton("新增") { _, _ ->
                    solution.id = UUID.randomUUID().toString()
                    solution.updateDateStr = TimeUtils.getNowString()
                    sharedModel.saveSolution(solution)
                    ToastUtils.showLong("方案新增成功")
                }
                .setCancelable(false)
                .show()
        } else {
            sharedModel.saveSolution(solution)
            ToastUtils.showLong("方案保存成功")
        }
    }

}

