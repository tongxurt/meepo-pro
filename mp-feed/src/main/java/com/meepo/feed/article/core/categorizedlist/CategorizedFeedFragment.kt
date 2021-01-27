package com.meepo.feed.article.core.categorizedlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.meepo.feed.R
import com.meepo.feed.Store
import com.meepo.feed.article.core.list.FeedListFragment
import com.meepo.feed.schema.Category
import com.meepo.sdk.framework.IPresenter
import com.meepo.sdk.framework.fragment.LoadOnceOnVisibleFragment
import kotlinx.android.synthetic.main.categorized_feed_fragment.*

/**
 * @author  佟旭
 * @wechat tongxury
 * @date  2020/11/13 11:34 AM
 * @version 1.0
 */
class CategorizedFeedFragment : LoadOnceOnVisibleFragment<IContract.IFeedPresenter>(), IContract.IFeedView {

    override fun applyCategories(categories: ArrayList<Category>?) {

        val mFragments = ArrayList<Fragment>()
        val mTitles = ArrayList<String>()

        categories?.forEach {
            mFragments.add(FeedListFragment(it))
            mTitles.add(it.name)
        }

        slidable_tabs.setViewPager(view_pager, mTitles, requireActivity(), mFragments)
    }

    override fun loadData() {
        mPresenter?.fetchCategories()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
    }

    override fun setUpContentLayout(): Int = R.layout.categorized_feed_fragment

    override fun setUpPresenter(): IPresenter? = Presenter()
}