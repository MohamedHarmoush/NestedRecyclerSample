package com.example.nestedrecyclersample.ui.adapter

import android.os.Parcelable
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nestedrecyclersample.R
import com.example.nestedrecyclersample.data.domain.AnimalSection
import com.example.nestedrecyclersample.ui.adapter.base.BaseAdapter
import com.example.nestedrecyclersample.ui.adapter.base.BaseViewHolder

class AnimalSectionAdapter(
    items: List<AnimalSection> = emptyList(),
) : BaseAdapter<AnimalSection>(
    R.layout.item_animal_section,
    items,
) {
    private val scrollStates = mutableMapOf<Int, Parcelable?>()

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onViewRecycled(holder: BaseViewHolder<AnimalSection>) {
        super.onViewRecycled(holder)

        //save horizontal scroll state
        val key = holder.layoutPosition
        scrollStates[key] =
            holder.itemView.findViewById<RecyclerView>(R.id.titledSectionRecycler).layoutManager?.onSaveInstanceState()
    }


    override fun bind(
        itemView: View,
        item: AnimalSection,
        position: Int,
        viewHolder: BaseViewHolderImp
    ) {
        itemView.findViewById<TextView>(R.id.sectionTitleTextView)?.text = item.title
        val layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

        val titledSectionRecycler = itemView.findViewById<RecyclerView>(R.id.titledSectionRecycler)
        titledSectionRecycler?.run {
            this.setRecycledViewPool(viewPool)
            this.layoutManager = layoutManager
            this.adapter = AnimalAdapter(item.animals)
        }

        //restore horizontal scroll state
        val state = scrollStates[viewHolder.layoutPosition]
        if (state != null) {
            titledSectionRecycler.layoutManager?.onRestoreInstanceState(state)
        } else {
            titledSectionRecycler.layoutManager?.scrollToPosition(0)
        }
    }
}