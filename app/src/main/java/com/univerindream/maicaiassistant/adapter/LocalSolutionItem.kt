package com.univerindream.maicaiassistant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.Utils
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import com.univerindream.maicaiassistant.MCUtil
import com.univerindream.maicaiassistant.R
import com.univerindream.maicaiassistant.data.DataRepository
import com.univerindream.maicaiassistant.databinding.ListLocalSolutionBinding
import com.univerindream.maicaiassistant.model.MCSolution
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class LocalSolutionItem(model: MCSolution) :
    ModelAbstractBindingItem<MCSolution, ListLocalSolutionBinding>(model) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface DataRepositoryEntryPoint {
        fun dataRepository(): DataRepository
    }

    val dataRepository: DataRepository by lazy {
        EntryPointAccessors.fromApplication(Utils.getApp(), DataRepositoryEntryPoint::class.java)
            .dataRepository()
    }

    override val type: Int
        get() = R.id.localFragment

    override fun bindView(binding: ListLocalSolutionBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)

        val id = MCUtil.getUUID8(model.id).ifBlank { "-" }
        var name = model.name.ifBlank { "-" }
        val author = model.author.ifBlank { "-" }
        val appVersion = model.appVersion.ifBlank { "-" }
        val updateDateStr = model.updateDateStr.ifBlank { "-" }
        val desc = model.desc

        if (model.id == dataRepository.curSolution.id) {
            name = "当前方案 => $name"
            binding.solutionName.setTextColor(ColorUtils.getColor(R.color.teal_700))
        } else {
            binding.solutionName.setTextColor(ColorUtils.getColor(R.color.black))
        }
        binding.solutionName.text = name

        val info = "作者: $author\nID: $id\n版本: $appVersion\n更新: $updateDateStr\n描述: \n$desc"
        binding.solutionDesc.text = info
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ListLocalSolutionBinding {
        return ListLocalSolutionBinding.inflate(inflater, parent, false)
    }

}