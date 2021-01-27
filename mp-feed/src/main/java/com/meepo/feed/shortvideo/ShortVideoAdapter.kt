package com.meepo.feed.shortvideo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.dueeeke.videocontroller.component.PrepareView
import com.meepo.design.list.ViewHolder
import com.meepo.feed.R
import com.meepo.feed.Store
import com.meepo.feed.schema.Item
import com.meepo.feed.shortvideo.ShortVideoAdapter.VideoHolder

class ShortVideoAdapter(val mContext: Context, private val mItems: ArrayList<Item>) : RecyclerView.Adapter<VideoHolder>() {
    private var mOnItemChildClickListener: OnItemChildClickListener? = null
    private var mOnItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.video_short_list_item_controller_layout, parent, false)
        return VideoHolder(mContext, itemView)
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        val mItem = mItems[position]
        // 封面
        holder.setImagePath(R.id.thumb, mItem.findOneCover(), imageLoader)
        // 标题
        holder.getView<TextView>(R.id.title_tv)?.let {
            it.text = mItem.title
        }

        holder.mPosition = position
    }

    private var imageLoader = object : ViewHolder.ImageLoader {
        override fun loadImage(iv: ImageView, path: String) {
            Glide.with(mContext)
                .load(path)
                .placeholder(android.R.color.darker_gray)
                .into(iv)

            Glide.with(mContext).load(path)
                .placeholder(android.R.color.darker_gray)
                .apply(
                    RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                ).transition(DrawableTransitionOptions().crossFade(500))
                .into(iv)

        }
    }


    override fun getItemCount(): Int {
        return mItems.size
    }

    fun addData(videoList: List<Item>) {
        val size = mItems.size
        mItems.addAll(videoList)
        //使用此方法添加数据，使用notifyDataSetChanged会导致正在播放的视频中断
        notifyItemRangeInserted(size, mItems.size)
    }

    inner class VideoHolder internal constructor(mContext: Context, itemView: View) : ViewHolder(mContext, itemView), View.OnClickListener {
        var mPosition = 0
        var mPlayerContainer: FrameLayout
        private var mTitle: TextView
        private var mThumb: ImageView
        var mPrepareView: PrepareView
        override fun onClick(v: View) {
            if (v.id == R.id.player_container) {
                if (mOnItemChildClickListener != null) {
                    mOnItemChildClickListener!!.onItemChildClick(mPosition)
                }
            } else {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener!!.onItemClick(mPosition)
                }
            }
        }

        init {
            mPlayerContainer = itemView.findViewById(R.id.player_container)
            mTitle = itemView.findViewById(R.id.title_tv)
            mPrepareView = itemView.findViewById(R.id.prepare_view)
            mThumb = mPrepareView.findViewById(R.id.thumb)
            if (mOnItemChildClickListener != null) {
                mPlayerContainer.setOnClickListener(this)
            }
            if (mOnItemClickListener != null) {
                itemView.setOnClickListener(this)
            }
            //通过tag将ViewHolder和itemView绑定
            itemView.tag = this
        }
    }

    fun setOnItemChildClickListener(onItemChildClickListener: OnItemChildClickListener?) {
        mOnItemChildClickListener = onItemChildClickListener
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = onItemClickListener
    }

}