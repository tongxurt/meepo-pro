package com.meepo.feed.article.core.search

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meepo.design.list.ViewHolder
import com.meepo.feed.R
import com.meepo.feed.Store
import com.meepo.feed.schema.SearchMetadata

class Adapter(private val context: Context, private val itemClickListener: OnItemClickListener?) :
    RecyclerView.Adapter<ViewHolder>() {

    private val contentList: ArrayList<SearchMetadata.SearchKeyword> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view = View.inflate(context, R.layout.search_activity_item, null)

        return ViewHolder(context, view)
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = contentList[position]
        holder.getView<TextView>(R.id.word_tv)?.let {
            it.text = data.name
            it.setOnClickListener {
                itemClickListener?.onItemClick(data, position)
            }
        }

    }

    fun addMore(items: List<SearchMetadata.SearchKeyword>) {
        contentList.addAll(items)
        notifyItemRangeInserted(this.contentList.size-items.size, items.size)
    }

    interface OnItemClickListener {
        fun onItemClick(value: SearchMetadata.SearchKeyword, position: Int)
    }
}