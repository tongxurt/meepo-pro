package com.meepo.feed.article.core.list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.meepo.feed.R
import com.meepo.feed.Store
import com.meepo.feed.Store.REFRESH_DELAY
import com.meepo.feed.schema.Category
import com.meepo.feed.schema.FeedResult
import com.meepo.feed.schema.Item
import com.meepo.sdk.framework.IPresenter
import com.meepo.sdk.framework.fragment.LoadOnceOnVisibleFragment
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.feed_list_fragment.*

/**
 * @author  佟旭
 * @wechat tongxury
 * @date  2020/11/8 6:45 PM
 * @version 1.0
 */
class FeedListFragment(val mCategory: Category) : LoadOnceOnVisibleFragment<Presenter>(), IContract.IFeedListView {

    private lateinit var mFeedListAdapter: FeedListAdapter

    override fun refresh(rsp: FeedResult?) {
        rsp?.items?.let {

            mFeedListAdapter?.refresh(it)
            stateful_layout.showContent()
            refresh_layout.finishRefresh(REFRESH_DELAY)
            return
        }

        refresh_layout.finishRefresh(false)
        stateful_layout.showError()
    }

    override fun append(rsp: FeedResult?) {
        rsp?.items?.let {
            mFeedListAdapter.addMore(it)
            stateful_layout.showContent()
            refresh_layout.finishLoadMore()
            return
        }
        refresh_layout.finishLoadMore(0, true, true)

    }

    override fun loadData() {
        Log.e("loadData", mCategory.id)
        mPresenter?.refresh(mCategory.id)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

        mFeedListAdapter =
            FeedListAdapter(this.requireContext())

        mFeedListAdapter.mItemListener = object : FeedListAdapter.ItemListener {
            override fun OnRemove(item: Item) {
                mPresenter?.remove(item.id)
            }
        }
        recycler_view.adapter = mFeedListAdapter




        stateful_layout.showLoading()

        recycler_view.layoutManager = LinearLayoutManager(this.requireContext())

        refresh_layout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPresenter?.fetchMore(mCategory.id)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPresenter?.refresh(mCategory.id)
            }
        })

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        Log.e("FeedListFragment", "onHiddenChanged")
    }


    override fun setUpContentLayout(): Int = R.layout.feed_list_fragment

    override fun setUpPresenter(): IPresenter? = Presenter()

}