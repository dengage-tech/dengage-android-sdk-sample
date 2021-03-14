package com.dengage.android.kotlin.sample.ui.adapter

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.dengage.android.kotlin.sample.R
import com.dengage.android.kotlin.sample.model.EventParameter
import com.dengage.android.kotlin.sample.ui.base.BaseRecyclerViewAdapter
import com.dengage.android.kotlin.sample.ui.base.BaseRecyclerViewHolder

/**
 * Created by Batuhan Coskun on 03 December 2020
 */
class EventParametersAdapter(
    items: List<EventParameter> = arrayListOf()
) : BaseRecyclerViewAdapter(items) {
    override fun createNewViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent)

    inner class ViewHolder(
        parent: ViewGroup
    ) : BaseRecyclerViewHolder<EventParameter>(parent, R.layout.recycler_item_custom_event) {

        override fun bindItem(item: EventParameter) {

            itemView.findViewById<AppCompatEditText>(R.id.etEventKey).addTextChangedListener {
                item.key = it.toString()
            }
            itemView.findViewById<AppCompatEditText>(R.id.etEventValue).addTextChangedListener {
                item.value = it.toString()
            }

        }
    }
}