package com.univerindream.maicaiassistant.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.univerindream.maicaiassistant.MCData
import com.univerindream.maicaiassistant.MCUtil
import com.univerindream.maicaiassistant.adapter.LocalSolutionItem
import com.univerindream.maicaiassistant.databinding.FragmentLocalBinding
import com.univerindream.maicaiassistant.model.MCSolution
import com.univerindream.maicaiassistant.viewmodels.LocalSolutionViewModel
import com.univerindream.maicaiassistant.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.util.*

@AndroidEntryPoint
class LocalFragment : Fragment() {

    private val sharedModel: SharedViewModel by activityViewModels()
    private val viewModel: LocalSolutionViewModel by viewModels()

    private var apiJob: Job? = null

    private val itemAdapter: ItemAdapter<LocalSolutionItem> by lazy {
        ItemAdapter()
    }
    private val fastAdapter by lazy {
        FastAdapter.with(itemAdapter)
    }

    private var exportSolution: MCSolution? = null
    private val exportARLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                try {
                    val result = activityResult.data?.data.toString()
                    LogUtils.v(result)
                    Utils.getApp().contentResolver.openFileDescriptor(Uri.parse(result), "rwt")?.use {

                        val fileOutputStream = FileOutputStream(it.fileDescriptor)

                        val bufferedWriter = FileOutputStream(it.fileDescriptor).bufferedWriter()
                        bufferedWriter.write(GsonUtils.toJson(exportSolution!!))
                        bufferedWriter.close()
                        fileOutputStream.close()

                        ToastUtils.showShort("方案导出成功")
                    }
                } catch (e: Exception) {
                    LogUtils.e(e, e.message)
                    ToastUtils.showLong("方案导出失败")
                }
            }
        }
    private val importARLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                val result = activityResult.data?.data.toString()
                LogUtils.i(result)

                try {
                    val uri = Uri.parse(result)
                    val input = Utils.getApp().contentResolver.openInputStream(uri)
                    val bufferedReader = input!!.bufferedReader()
                    val json = bufferedReader.readText()
                    bufferedReader.close()

                    val solution = GsonUtils.fromJson(json, MCSolution::class.java)
                    addOrReplaceSolution(solution)
                } catch (e: Exception) {
                    ToastUtils.showLong("方案导入失败")
                }

            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLocalBinding.inflate(inflater, container, false)

        binding.localSolutions.adapter = fastAdapter
        binding.localSolutions.layoutManager = LinearLayoutManager(requireContext())
        fastAdapter.onClickListener = { _, adapter, item, position ->
            val oidPos = adapter.adapterItems.indexOfFirst { it.model.id == sharedModel.curSolution.value?.id }
            sharedModel.setCurSolution(item.model)
            if (oidPos > -1) fastAdapter.notifyAdapterItemChanged(oidPos)
            fastAdapter.notifyAdapterItemChanged(position)
            false
        }
        fastAdapter.onLongClickListener = { view, _, item, position ->
            val solutionId = item.model.id

            var items = arrayOf("修改", "复制", "导出", "分享", "删除")
            if (sharedModel.curSolution.value?.id == solutionId) {
                items = arrayOf("修改", "复制", "导出", "分享")
            }

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("操作")
                .setItems(items) { dialog, which ->
                    when (items[which]) {
                        "修改" -> {
                            val action =
                                LocalFragmentDirections.actionLocalFragmentToSolutionFragment(solutionId)
                            findNavController().navigate(action)
                        }
                        "JSON" -> {
                            val action =
                                LocalFragmentDirections.actionLocalFragmentToJsonFragment(solutionId)
                            findNavController().navigate(action)
                        }
                        "复制" -> {
                            val json = GsonUtils.toJson(item.model)
                            ClipboardUtils.copyText(JsonUtils.formatJson(json, 0))
                            ToastUtils.showLong("已将内容复制到剪切板，可以分享给别人啦")
                        }
                        "导出" -> {
                            exportSolution = item.model
                            exportARLauncher.launch(Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                                val fileName = "${item.model.name}_${MCUtil.getUUID8(item.model.id)}.json"
                                addCategory(Intent.CATEGORY_OPENABLE)
                                type = "application/json"
                                putExtra(Intent.EXTRA_TITLE, fileName)
                            })
                        }
                        "分享" -> {
                            shareSolution(item.model)
                        }
                        "删除" -> {
                            val res = sharedModel.deleteSolution(solutionId)
                            if (res) {
                                ToastUtils.showShort("删除成功")
                            } else {
                                ToastUtils.showShort("删除失败")
                            }
                        }
                    }
                    dialog.dismiss()
                }
                .show()
            false
        }
        binding.floatingAddButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("新增")
                .setItems(arrayOf("空白", "粘贴", "导入")) { dialog, which ->
                    when (which) {
                        0 -> {
                            addOrReplaceSolution(MCSolution(name = "空白"))
                        }
                        1 -> {
                            val content = ClipboardUtils.getText()?.toString()
                            if (content.isNullOrBlank()) {
                                ToastUtils.showLong("未读取到剪切板内容")
                            } else {
                                try {
                                    val solution = GsonUtils.fromJson(content, MCSolution::class.java)
                                    addOrReplaceSolution(solution)
                                } catch (e: Exception) {
                                    ToastUtils.showLong("剪切板内容非法")
                                }
                            }
                        }
                        2 -> {
                            importARLauncher.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                type = "application/json"
                                addCategory(Intent.CATEGORY_OPENABLE)
                            })
                        }
                    }
                    dialog.dismiss()
                }
                .show()
        }

        subscribeUI()

        return binding.root
    }

    private fun subscribeUI() {
        sharedModel.localSolutions.observe(viewLifecycleOwner) {
            LogUtils.d("localSolutions")
            itemAdapter.clear()
            itemAdapter.add(it.map { LocalSolutionItem(it) })
        }
    }

    private fun getPublicSolutions() {
        apiJob?.cancel()
        apiJob = lifecycleScope.launch {
            viewModel.getPublicSolutions().collectLatest {
                if (it.isSuccess) {
                    val solutions = it.getOrNull() ?: return@collectLatest
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("远程方案")
                        .setItems(
                            solutions
                                .map {
                                    val id = MCUtil.getUUID8(it.id).ifBlank { "-" }
                                    val name = it.name.ifBlank { "-" }
                                    val author = it.author.ifBlank { "-" }
                                    val appVersion = it.appVersion.ifBlank { "-" }
                                    val updateDateStr = it.updateDateStr.ifBlank { "-" }
                                    val desc = it.desc.ifBlank { "-" }
                                    "\n------------\n方案名: $name\n作者: $author\nID: $id\n版本: $appVersion\n更新: $updateDateStr\n描述: \n$desc\n------------\n"
                                }
                                .toTypedArray()
                        )
                        { _, which ->
                            val solution = solutions[which]
                            addOrReplaceSolution(solution)
                        }
                        .show()
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

    private fun shareSolution(solution: MCSolution) {
        if (MCData.isHintedShareAuthor) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.data = Uri.parse("mailto:")
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("maicaiassistant@hotmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "方案分享")
            intent.putExtra(Intent.EXTRA_TEXT, GsonUtils.toJson(solution))
            startActivity(Intent.createChooser(intent, "分享一下"))
        } else {
            MCData.isHintedShareAuthor = true
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("提示")
                .setMessage("邮件的方式，可以分享给作者哟")
                .setPositiveButton("知道了") { dialog, which ->
                    shareSolution(solution)
                }
                .setCancelable(false)
                .show()
        }
    }
}

