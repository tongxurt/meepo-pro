package com.meepo.feed.article.core.search.list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.meepo.feed.R
import com.meepo.feed.Store
import com.meepo.feed.schema.FeedResult
import com.meepo.sdk.base.mvp.BaseFragment
import com.meepo.sdk.base.mvp.IBaseContract
import kotlinx.android.synthetic.main.search_list_fragment.refresh_layout
import kotlinx.android.synthetic.main.search_list_fragment.stateful_layout
import kotlinx.android.synthetic.main.search_list_fragment.recycler_view

class SearchListFragment(private val keyword: String) : BaseFragment<Presenter>(), IContract.IView {

    private var mFeedListAdapter: SearchListAdapter? = null

    override fun doFinally() {
        super.doFinally()
        refresh_layout.finishLoadMore(Store.LOAD_MORE_DELAY)
        stateful_layout.showContent()
    }

    override fun append(rsp: FeedResult?) {
        rsp?.items?.let {
            mFeedListAdapter?.addMore(it)
        }
    }

    override fun initData() {
        mPresenter?.fetchMore(keyword)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        Log.e("SearchListFragment", "initView")

        mFeedListAdapter = SearchListAdapter(this.requireContext())
        recycler_view.adapter = mFeedListAdapter
        stateful_layout.showLoading()

        recycler_view.layoutManager = LinearLayoutManager(this.requireContext())

        refresh_layout.setEnableRefresh(false)
        refresh_layout.setOnLoadMoreListener { mPresenter?.fetchMore(keyword) }
    }

    override fun setUpContentLayout(): Int = R.layout.search_list_fragment

    override fun setUpPresenter(): IBaseContract.IBasePresenter? = Presenter()
}