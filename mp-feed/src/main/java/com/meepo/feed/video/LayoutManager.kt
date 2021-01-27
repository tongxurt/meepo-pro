package com.meepo.feed.video;

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class LayoutManager : LinearLayoutManager, RecyclerView.OnChildAttachStateChangeListener {

    constructor(
        context: Context, @RecyclerView.Orientation orientation: Int,
        reverseLayout: Boolean
    ) : super(context, orientation, reverseLayout)

    private var pagerSnapHelper: PagerSnapHelper = PagerSnapHelper()
    private var viewPagerListener: OnViewPagerListener? = null
    private var diffY = 0   // 判断滑动方向

    override fun onAttachedToWindow(view: RecyclerView) {
        Log.e("LayoutManager", "onAttachedToWindow")

        super.onAttachedToWindow(view)
        viewPagerListener?.onInitComplete()

        view.addOnChildAttachStateChangeListener(this)
        pagerSnapHelper.attachToRecyclerView(view)

    }

    // Detached
    override fun onChildViewDetachedFromWindow(view: View) {
        Log.e("LayoutManager", "onChildViewDetachedFromWindow")

        val position = getPosition(view)
        viewPagerListener?.onPageRelease(0 < diffY, position)
    }

    // Attached
    override fun onChildViewAttachedToWindow(view: View) {

        Log.e("LayoutManager", "onChildViewAttachedToWindow")

        val position = getPosition(view)

        viewPagerListener?.onPageSelected(position, 0 == position)

//        if (0 == position) {
//            viewPagerListener?.onPageSelected(position, false)
//        }
    }

    override fun onScrollStateChanged(state: Int) {
        // 静止没有滚动
        if (RecyclerView.SCROLL_STATE_IDLE == state) {
            pagerSnapHelper.findSnapView(this)?.let {
                val position = getPosition(it)
                viewPagerListener?.onPageSelected(position, position == itemCount - 1)
            }
        }
        super.onScrollStateChanged(state)
    }

    public fun setOnViewPagerListener(listener: OnViewPagerListener) {
        viewPagerListener = listener
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        diffY = dy
        return super.scrollVerticallyBy(dy, recycler, state)
    }

}