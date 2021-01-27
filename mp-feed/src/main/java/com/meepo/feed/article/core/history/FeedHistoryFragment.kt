package com.meepo.feed.article.core.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.meepo.feed.R
import com.meepo.feed.Store
import com.meepo.feed.article.core.history.list.HistoryListFragment
import com.meepo.sdk.framework.fragment.StaticFragment
import kotlinx.android.synthetic.main.history_feed_fragment.slidable_tabs
import kotlinx.android.synthetic.main.history_feed_fragment.view_pager

class FeedHistoryFragment(private val initialCategoryId: String?) : StaticFragment() {

    override fun initView(view: View, savedInstanceState: Bundle?) {
        val mFragments = ArrayList<Fragment>()
        val mTitles = ArrayList<String>()

        var initialTab = 0

        Store.State.historyTabs.forEachIndexed { index, category ->
            mFragments.add(HistoryListFragment(category))
            mTitles.add(category.name)

            if (!initialCategoryId.isNullOrEmpty() && initialCategoryId == category.id) initialTab = index

        }

        slidable_tabs.setViewPager(view_pager, mTitles, requireActivity(), mFragments)
        slidable_tabs.currentTab = initialTab
    }


    override fun setUpContentLayout(): Int = R.layout.history_feed_fragment
}