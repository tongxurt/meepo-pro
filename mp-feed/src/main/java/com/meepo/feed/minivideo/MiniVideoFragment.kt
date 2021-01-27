package com.meepo.feed.minivideo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import com.dueeeke.videoplayer.ijk.IjkPlayer
import com.dueeeke.videoplayer.player.VideoView
import com.meepo.feed.R
import com.meepo.feed.minivideo.cache.PreloadManager
import com.meepo.feed.minivideo.cache.ProxyVideoCacheManager
import com.meepo.feed.minivideo.widget.TikTokRenderViewFactory
import com.meepo.feed.minivideo.widget.VerticalViewPager
import com.meepo.feed.schema.FeedResult
import com.meepo.feed.schema.Item
import com.meepo.sdk.framework.IPresenter
import com.meepo.sdk.framework.fragment.LoadOnceOnVisibleFragment
import kotlinx.android.synthetic.main.video_mini_list_fragment.*

class MiniVideoFragment private constructor() : LoadOnceOnVisibleFragment<Presenter>(), IContract.IFeedListView {
    /**
     * 当前播放位置
     */
    private var mCurPos = 0
    private val mVideoList: ArrayList<Item> = ArrayList()
    private lateinit var mMiniVideoAdapter: MiniVideoAdapter
    private var mVideoView: VideoView<IjkPlayer>? = null

    private lateinit var mController: MiniVideoController

    override fun refresh(rsp: FeedResult?) {
        rsp?.items?.let {
            addData(it)

            vertical_view_pager.currentItem = 0

            vertical_view_pager.post { startToPlay(0) }
        }
    }

    override fun append(rsp: FeedResult?) {
        rsp?.items?.let {
            addData(it)
        }
    }


    private fun addData(items: List<Item>) {
//        mVideoList.addAll(DataUtil.getTiktokDataFromAssets(this.requireContext()))
        mVideoList.addAll(items)
        mMiniVideoAdapter.notifyDataSetChanged()
    }


    override fun initView(view: View, savedInstanceState: Bundle?) {
        initVideoView()
        initViewPager()
    }

    private fun initVideoView() {
        mVideoView = VideoView<IjkPlayer>(this.requireContext())
        mVideoView?.setLooping(true)

        //以下只能二选一，看你的需求
        mVideoView?.setRenderViewFactory(TikTokRenderViewFactory.create())
        //        mVideoView?.setScreenScaleType(VideoView.SCREEN_SCALE_CENTER_CROP);
        mController = MiniVideoController(this.requireContext())
        mVideoView?.setVideoController(mController)
    }


    private fun initViewPager() {
        vertical_view_pager.offscreenPageLimit = 4
        mMiniVideoAdapter = MiniVideoAdapter(mVideoList)
        vertical_view_pager.adapter = mMiniVideoAdapter
        vertical_view_pager.overScrollMode = View.OVER_SCROLL_NEVER
        vertical_view_pager.setOnPageChangeListener(object : SimpleOnPageChangeListener() {
            private var mCurItem = 0

            /**
             * VerticalViewPager是否反向滑动
             */
            private var mIsReverseScroll = false
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (position == mCurItem) {
                    return
                }
                mIsReverseScroll = position < mCurItem
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                Log.e("onPageSelected", "mIsReverseScroll: $mIsReverseScroll  mVideoList.size: ${mVideoList.size}  pos: $position mCurPos: $mCurPos")

                if ((mVideoList.size == 0) || position >= mCurItem && mVideoList.size - position - 1 < 5) {
                    mPresenter?.fetchMore("")
                }

                if (position == mCurPos) return
                startToPlay(position)


            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == VerticalViewPager.SCROLL_STATE_DRAGGING) {
                    mCurItem = vertical_view_pager.currentItem
                }
                if (state == VerticalViewPager.SCROLL_STATE_IDLE) {
                    PreloadManager.instance(requireContext()).resumePreload(mCurPos, mIsReverseScroll)
                } else {
                    PreloadManager.instance(requireContext()).pausePreload(mCurPos, mIsReverseScroll)
                }
            }
        })
    }

    private fun startToPlay(position: Int) {
        val count = vertical_view_pager.childCount

        for (i in 0 until count) {
            val itemView = vertical_view_pager.getChildAt(i)
            val viewHolder = itemView.tag as MiniVideoAdapter.VideoViewHolder
            if (viewHolder.mPosition == position) {
                mVideoView?.release()
                Utils.removeViewFormParent(mVideoView)
                val tiktokBean = mVideoList[position]
                val playUrl = PreloadManager.instance(requireContext()).getLocalPlayUrl(tiktokBean.content?.content)
                mVideoView?.setUrl(playUrl)
                mController.addControlComponent(viewHolder.mFullVideoView, true)
                viewHolder.mPlayerContainer.addView(mVideoView, 0)
                mVideoView?.start()

                mCurPos = position
                break
            }
        }
    }


    // 页面切换事件
    override fun onHiddenChanged(hidden: Boolean) {

        Log.e("onHiddenChanged", "" + hidden)
        super.onHiddenChanged(hidden)
        if (hidden) {
            mVideoView?.pause()
        } else {
            mVideoView?.resume()
        }
    }

    fun pauseCurrentIfExists() {
        Log.e("MVF", "pauseCurrentIfExists")
        mVideoView?.pause()
    }

    fun resumeCurrentIfExistsAndVisible() {
        Log.e("MVF", "resumeCurrentIfExistsAndVisible $mVisible")

        if (mVisible) {
            mVideoView?.resume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PreloadManager.instance(requireContext()).removeAllPreloadTask()
        //清除缓存，实际使用可以不需要清除，这里为了方便测试
        ProxyVideoCacheManager.clearAllCache(this.requireContext())
    }

    override fun loadData() {
        mPresenter?.refresh("")
    }

    override fun setUpContentLayout(): Int = R.layout.video_mini_list_fragment
    override fun setUpPresenter(): IPresenter? = Presenter()


    companion object {
        private var miniVideoFragment: MiniVideoFragment? = null

        fun instance(): MiniVideoFragment {

            if (miniVideoFragment == null) {
                miniVideoFragment = MiniVideoFragment()
            }

            return miniVideoFragment!!
        }
    }
}