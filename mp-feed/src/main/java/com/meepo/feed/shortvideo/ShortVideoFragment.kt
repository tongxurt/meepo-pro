package com.meepo.feed.shortvideo

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import com.dueeeke.videocontroller.StandardVideoController
import com.dueeeke.videocontroller.component.*
import com.dueeeke.videoplayer.ijk.IjkPlayer
import com.dueeeke.videoplayer.player.VideoView
import com.dueeeke.videoplayer.player.VideoView.SimpleOnStateChangeListener
import com.dueeeke.videoplayer.player.VideoViewManager
import com.meepo.feed.R
import com.meepo.feed.Store
import com.meepo.feed.minivideo.Utils
import com.meepo.feed.minivideo.cache.ProxyVideoCacheManager
import com.meepo.feed.schema.FeedResult
import com.meepo.feed.schema.Item
import com.meepo.sdk.framework.IPresenter
import com.meepo.sdk.framework.fragment.LoadOnceOnVisibleFragment
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.video_short_list_fragment.recycler_view
import kotlinx.android.synthetic.main.video_short_list_fragment.refresh_layout
import kotlinx.android.synthetic.main.video_short_list_fragment.stateful_layout

class ShortVideoFragment : LoadOnceOnVisibleFragment<Presenter>(), IContract.IFeedListView, OnItemChildClickListener {

    private var mVideos: ArrayList<Item> = ArrayList()
    private lateinit var mAdapterShort: ShortVideoAdapter
    private lateinit var mLinearLayoutManager: LinearLayoutManager

    private var mVideoView: VideoView<IjkPlayer>? = null
    private lateinit var mController: StandardVideoController
    private var mErrorView: ErrorView? = null
    private var mCompleteView: CompleteView? = null
    private var mTitleView: TitleView? = null


    /**
     * 当前播放的位置
     */
    private var mCurPos = -1

    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    private var mLastPos = mCurPos

    override fun refresh(rsp: FeedResult?) {
        rsp?.items?.let {
            mVideos.addAll(it)
            mAdapterShort.notifyDataSetChanged()
            stateful_layout.showContent()
            refresh_layout.finishRefresh(Store.REFRESH_DELAY)
            return
        }

        refresh_layout.finishRefresh(false)
        stateful_layout.showError()
    }

    override fun append(rsp: FeedResult?) {
        rsp?.items?.let {
            mAdapterShort.addData(it)
            stateful_layout.showContent()
            refresh_layout.finishLoadMore()
            return
        }
        refresh_layout.finishLoadMore(0, true, true)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        initVideoView()


        mLinearLayoutManager = LinearLayoutManager(context)
        recycler_view.layoutManager = mLinearLayoutManager

        mAdapterShort = ShortVideoAdapter(requireContext(), mVideos)
        mAdapterShort.setOnItemChildClickListener(this)

        recycler_view.adapter = mAdapterShort

        recycler_view.addOnChildAttachStateChangeListener(object : OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {}
            override fun onChildViewDetachedFromWindow(view: View) {
                view.findViewById<FrameLayout>(R.id.player_container)?.let {
                    val v = it.getChildAt(0)
                    if (v != null && v === mVideoView && !mVideoView!!.isFullScreen) {
                        releaseVideoView()
                    }
                }
            }
        })

        stateful_layout.showLoading()

        refresh_layout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPresenter?.fetchMore("")
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPresenter?.refresh("")
            }
        })

    }

    private fun initVideoView() {
        mVideoView = VideoView(requireActivity())
        mVideoView?.setOnStateChangeListener(object : SimpleOnStateChangeListener() {
            override fun onPlayStateChanged(playState: Int) {
                //监听VideoViewManager释放，重置状态
                if (playState == VideoView.STATE_IDLE) {
                    Utils.removeViewFormParent(mVideoView)
                    mLastPos = mCurPos
                    mCurPos = -1
                }
            }
        })
        mController = StandardVideoController(requireActivity())
        mErrorView = ErrorView(requireActivity())
        mController.addControlComponent(mErrorView)
        mCompleteView = CompleteView(requireActivity())
        mController.addControlComponent(mCompleteView)
        mTitleView = TitleView(requireActivity())
        mController.addControlComponent(mTitleView)
        mController.addControlComponent(VodControlView(requireActivity()))
        mController.addControlComponent(GestureView(requireActivity()))
        mController.setEnableOrientation(true)
        mVideoView?.setVideoController(mController)
    }

    /**
     * 开始播放
     * @param position 列表位置
     */
    private fun startPlay(position: Int) {
        if (mCurPos == position) return
        if (mCurPos != -1) {
            releaseVideoView()
        }
        val videoBean = mVideos[position]
        val playUrl = videoBean.content?.content
        //边播边存
        val proxyUrl = ProxyVideoCacheManager.getProxy(requireActivity()).getProxyUrl(playUrl)
        mVideoView!!.setUrl(proxyUrl)

        mVideoView!!.setUrl(playUrl)
        mTitleView!!.setTitle(videoBean.title)

        val itemView = mLinearLayoutManager.findViewByPosition(position) ?: return
        val viewHolderShort: ShortVideoAdapter.VideoHolder = itemView.tag as ShortVideoAdapter.VideoHolder
        //把列表中预置的PrepareView添加到控制器中，注意isPrivate此处只能为true。
        mController.addControlComponent(viewHolderShort.mPrepareView, true)
        Utils.removeViewFormParent(mVideoView)
        viewHolderShort.mPlayerContainer.addView(mVideoView, 0)
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        VideoViewManager.instance().add(mVideoView, "list")
        mVideoView!!.start()
        mCurPos = position
    }

    private fun releaseVideoView() {
        mVideoView!!.release()
        if (mVideoView!!.isFullScreen) {
            mVideoView!!.stopFullScreen()
        }
        if (requireActivity().requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        mCurPos = -1
    }


    override fun onPause() {
        super.onPause()
        pause()
    }

    /**
     * 由于onPause必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onPause的逻辑
     */
    private fun pause() {
        releaseVideoView()
    }

    override fun onResume() {
        super.onResume()
        resume()
    }

    /**
     * 由于onResume必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onResume的逻辑
     */
    private fun resume() {
        if (mLastPos == -1) return
//        if (MainActivity.mCurrentIndex !== 1) return
        //恢复上次播放的位置
        startPlay(mLastPos)
    }

    /**
     * PrepareView被点击
     */
    override fun onItemChildClick(position: Int) {
        startPlay(position)
    }

    override fun loadData() {

//        val videoList: List<VideoBean> = DataUtil.getVideoList()
//        mVideos.addAll(videoList)
//        mAdapterShort!!.notifyDataSetChanged()

        mPresenter?.refresh("")

    }

    override fun setUpContentLayout(): Int = R.layout.video_short_list_fragment

    override fun setUpPresenter(): IPresenter? = Presenter()

    companion object {
        private var shortVideoFragment: ShortVideoFragment? = null
        fun instance(): ShortVideoFragment {
            if (shortVideoFragment == null) {
                shortVideoFragment = ShortVideoFragment()
            }
            return shortVideoFragment!!
        }
    }

}