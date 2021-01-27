package com.meepo.feed.video

import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.VideoView
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.meepo.design.helper.StatusBarHelper
import com.meepo.feed.R
import com.meepo.feed.Store
import com.meepo.feed.schema.FeedResult
import com.meepo.sdk.framework.IPresenter
import com.meepo.sdk.framework.fragment.LoadOnceOnVisibleFragment
import kotlinx.android.synthetic.main.video_list_frament.*

class VideoFragment : LoadOnceOnVisibleFragment<Presenter>(), IContract.IFeedListView {
    private lateinit var mAdapter: ListAdapter
    private lateinit var layoutManager: LayoutManager


    override fun refresh(rsp: FeedResult?) {
        rsp?.items?.let {
            mAdapter.addMore(it)
            Log.e("aaaaaaa", "addmore")

//            playVideo(0)
        }
    }

    override fun append(rsp: FeedResult?) {
        rsp?.items?.let {
            mAdapter.addMore(it)
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

        StatusBarHelper.immerse(this.requireActivity(), top_view)
        layoutManager = LayoutManager(this.requireContext(), RecyclerView.VERTICAL, false)

        mAdapter = ListAdapter(this.requireContext())
        recycler.adapter = mAdapter


        recycler.layoutManager = layoutManager


        layoutManager.setOnViewPagerListener(object : OnViewPagerListener {
            override fun onInitComplete() {
                Log.e("videofragment", "onInitComplete")
                mPresenter?.refresh("")
//                playVideo(0)

            }
            override fun onPageRelease(isNext: Boolean, position: Int) {
                Log.e("videofragment", "释放位置:$position 下一页:$isNext")
                var index = 0
                index = if (isNext) {
                    0
                } else {
                    1
                }

                releaseVideo(index)
            }

            override fun onPageSelected(position: Int, bottom: Boolean) {
                Log.e("videofragment", "选择位置:$position 是否到底部:$bottom")
//                mAdapter.addMore(items)
                if (bottom) {
                    mPresenter?.fetchMore("")
                }

                Log.e("onPageSelected", "playVideo")

                playVideo(0)
            }
        })
    }

    override fun loadData() {
//        mAdapter.addMore(items)

//        mPresenter?.refresh("")

    }

    override fun setUpContentLayout(): Int = R.layout.video_list_frament

    override fun setUpPresenter(): IPresenter? = Presenter()

    private fun releaseVideo(index: Int) {

        Log.e("releaseVideo", "" + index)

        val itemView: View = recycler.getChildAt(index)
        val videoView = itemView.findViewById<VideoView>(R.id.video_view)
        val imgThumb = itemView.findViewById<ImageView>(R.id.thumb_iv)
        val imgPlay = itemView.findViewById<ImageView>(R.id.play_iv)
        videoView.stopPlayback()
        imgThumb.animate().alpha(1f).start()
        imgPlay.animate().alpha(0f).start()
    }


    private fun playVideo(position: Int) {

//        Log.e("playVideo", "" + position + "      " + recycler.size)

        val itemView: View = recycler.getChildAt(position)
        val videoView: FullVideoView = itemView.findViewById(R.id.video_view)
        val imgThumb = itemView.findViewById<ImageView>(R.id.thumb_iv)
        val mediaPlayer = arrayOfNulls<MediaPlayer>(1)
        videoView.setOnPreparedListener(OnPreparedListener { })
        videoView.setOnInfoListener(MediaPlayer.OnInfoListener { mp, what, extra ->
            mediaPlayer[0] = mp
            mp.isLooping = true
            imgThumb.animate().alpha(0f).setDuration(0).start()
            false
        })
        videoView.start()

    }

}