package com.meepo.feed.minivideo

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.meepo.design.imageview.CircularImage
import com.meepo.design.list.ViewHolder
import com.meepo.design.text.icontext.IconText
import com.meepo.feed.R
import com.meepo.feed.Store
import com.meepo.feed.minivideo.cache.PreloadManager
import com.meepo.feed.minivideo.profile.ProfileActivity
import com.meepo.feed.minivideo.widget.FullVideoView
import com.meepo.feed.schema.Item
import com.meepo.sdk.helper.ActivityHelper
import java.util.*

class MiniVideoAdapter(private val mItems: List<Item>) : PagerAdapter() {
    /**
     * View缓存池，从ViewPager中移除的item将会存到这里面，用来复用
     */
    private val mViewPool: MutableList<View> = ArrayList()

    override fun getCount(): Int {
        return mItems.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

    @SuppressLint("SetTextI18n")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val mItem = mItems[position]

        val mContext = container.context
        var view: View? = null
        if (mViewPool.size > 0) { //取第一个进行复用
            view = mViewPool[0]
            mViewPool.removeAt(0)
        }
        val viewHolder: VideoViewHolder
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.video_mini_list_item_layout, container, false)
            viewHolder = VideoViewHolder(mContext, view)
        } else {
            viewHolder = view.tag as VideoViewHolder
        }

        //开始预加载
        mItem.content?.let {
            PreloadManager.instance(mContext).addPreloadTask(it.content, position)
        }

        // 封面
        mItem.covers?.let {
            if (it.isNotEmpty()) {
                Glide.with(mContext)
                    .load(it[0].content)
                    .placeholder(android.R.color.white)
                    .into(viewHolder.mThumb)
            }
        }


        // 标题
        viewHolder.getView<TextView>(R.id.video_title_tv)?.let {
            Log.e("title", mItem.title)
            it.text = mItem.title
            it.setOnClickListener { Toast.makeText(mContext, "点击了标题", Toast.LENGTH_SHORT).show() }
        }

        // 作者信息
        mItem.author?.let { author ->
            // 头像
            author.avatar?.let {
                viewHolder.getView<CircularImage>(R.id.avatar_ci)?.let { view ->
                    viewHolder.setImagePath(R.id.avatar_ci, it)

                    view.setOnClickListener {
                        gotoProfile(mContext)
                    }
                }
            }

            // 昵称
            viewHolder.getView<TextView>(R.id.nickname_tv)?.let {
                it.text = "@${author.nickname ?: ""}"

                it.setOnClickListener {
                    gotoProfile(mContext)
                }
            }
        }

        // 点赞
        viewHolder.getView<IconText>(R.id.like_it)?.let {
            it.title = "2.2w"
            it.iconResId = R.drawable.ic_heart_pink
        }


        viewHolder.mPosition = position
        container.addView(view)
        return view!!
    }

    private fun gotoProfile(context: Context) {
        MiniVideoFragment.instance().pauseCurrentIfExists()

        ActivityHelper.launch(context, ProfileActivity::class.java)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        val mItem = mItems[position]

        val itemView = `object` as View
        container.removeView(itemView)

        mItem.content?.let {
            //取消预加载
            PreloadManager.instance(container.context).removePreloadTask(it.content)
        }

        //保存起来用来复用
        mViewPool.add(itemView)
    }


    class VideoViewHolder internal constructor(mContext: Context, itemView: View) : ViewHolder(mContext, itemView) {
        var mPosition = 0

        //        var mTitle: TextView
        var mAvatar: CircularImage
        var mThumb: ImageView
        var mFullVideoView: FullVideoView
        var mPlayerContainer: FrameLayout

        init {
            mFullVideoView = itemView.findViewById(R.id.full_video_view)
//            mTitle = mFullVideoView.findViewById(R.id.title_tv)
            mAvatar = mFullVideoView.findViewById(R.id.avatar_ci)
            mThumb = mFullVideoView.findViewById(R.id.thumb_iv)
            mPlayerContainer = itemView.findViewById(R.id.container)
            itemView.tag = this
        }
    }

}