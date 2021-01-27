package com.meepo.feed.article.core.history.list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.meepo.feed.R
import com.meepo.feed.Store
import com.meepo.feed.schema.Category
import com.meepo.feed.schema.FeedResult
import com.meepo.sdk.framework.IPresenter
import com.meepo.sdk.framework.fragment.LoadOnceOnVisibleFragment
import kotlinx.android.synthetic.main.history_list_fragment.*

/**
 * @author  佟旭
 * @wechat tongxury
 * @date  2020/11/8 6:45 PM
 * @version 1.0
 */
class HistoryListFragment(private val mCategory: Category) : LoadOnceOnVisibleFragment<Presenter>(), IContract.IHistoryView {

    private lateinit var mFeedListAdapter: HistoryListAdapter

    override fun append(rsp: FeedResult?, page: Int) {

        rsp?.items?.let {
            mFeedListAdapter.addMore(it)
            stateful_layout.showContent()
            return
        }

        if (page <= 1) {
            stateful_layout.showEmpty()
        } else {
            refresh_layout.finishLoadMore(Store.LOAD_MORE_DELAY, true, true)
        }
    }

    override fun loadData() {
        Log.e("loadData", mCategory.id)
        mPresenter?.fetchMore(mCategory.id)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

        stateful_layout.showLoading()

        mFeedListAdapter = HistoryListAdapter(this.requireContext())
        recycler_view.adapter = mFeedListAdapter
        recycler_view.layoutManager = LinearLayoutManager(this.requireContext())
        refresh_layout.setEnableRefresh(false)
        refresh_layout.setOnLoadMoreListener { mPresenter?.fetchMore(mCategory.id) }
    }

    override fun setUpContentLayout(): Int = R.layout.history_list_fragment

    override fun setUpPresenter(): IPresenter? = Presenter()

}