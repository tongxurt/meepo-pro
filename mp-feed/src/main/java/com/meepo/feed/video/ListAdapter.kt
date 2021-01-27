package com.meepo.feed.video

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.meepo.design.list.ViewHolder
import com.meepo.feed.R
import com.meepo.feed.Store
import com.meepo.feed.schema.Item

internal class ListAdapter(private val mContext: Context) : RecyclerView.Adapter<ViewHolder>() {

    private val mItems = ArrayList<Item>()

    fun addMore(items: List<Item>) {
        this.mItems.addAll(items)
        notifyItemRangeInserted(this.mItems.size - items.size, items.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.video_item_view_pager, parent, false)
        return ViewHolder(mContext, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mItems[position]

        item.covers?.let {
            if (it.isNotEmpty()) {
                holder.setImagePath(R.id.thumb_iv, it[0].content ?: "")
            }
        }

        holder.getView<VideoView>(R.id.video_view)?.let {
            it.setVideoPath(item.content!!.content)

            it.setOnClickListener { _ ->
                Log.e("videoView ", "onClick ")

                if (it.isPlaying) {
                    holder.getView<ImageView>(R.id.play_iv)?.let { iv -> iv.animate()?.alpha(0.7f)?.start() }
                    it.pause()
                } else {
                    holder.getView<ImageView>(R.id.play_iv)?.let { iv -> iv.animate()?.alpha(0f)?.start() }
                    it.start()
                }

            }
        }

    }



    override fun getItemCount(): Int {
        return mItems.size
    }
}
