package com.univerindream.maicaiassistant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import com.univerindream.maicaiassistant.MCNodeMessage
import com.univerindream.maicaiassistant.R
import com.univerindream.maicaiassistant.databinding.ItemSearchNodeBinding

class BindingSearchNodeItem(model: MCNodeMessage) :
    ModelAbstractBindingItem<MCNodeMessage, ItemSearchNodeBinding>(model) {

    override val type: Int
        get() = R.id.adapter_search_node_item

    override fun bindView(binding: ItemSearchNodeBinding, payloads: List<Any>) {
        binding.nodeName.text = model.name
        binding.nodeValue.text = model.value.toString()
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemSearchNodeBinding {
        return ItemSearchNodeBinding.inflate(inflater, parent, false)
    }
}