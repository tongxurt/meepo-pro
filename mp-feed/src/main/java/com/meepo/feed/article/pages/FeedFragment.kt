package com.meepo.feed.article.pages

import android.os.Bundle
import android.util.Log
import android.view.View
import com.meepo.design.helper.StatusBarHelper
import com.meepo.feed.R
import com.meepo.feed.article.core.categorizedlist.CategorizedFeedFragment
import com.meepo.feed.article.core.search.SearchActivity
import com.meepo.feed.minivideo.MiniVideoFragment
import com.meepo.sdk.framework.fragment.StaticNestedFragment
import com.meepo.sdk.helper.ActivityHelper
import kotlinx.android.synthetic.main.feed_fragment.*

/**
 * @author  佟旭
 * @wechat tongxury
 * @date  11/13/20 10:10 PM
 * @version 1.0
 */
class FeedFragment private constructor(): StaticNestedFragment() {

    override fun initView(view: View, savedInstanceState: Bundle?) {

        StatusBarHelper.immerse(this.requireActivity(), header_rl)

        search_layout.setOnClickListener {

            Log.e("FeedFragment", "FeedFragment c")

            ActivityHelper.launch(this.requireContext(), SearchActivity::class.java)
        }


        val fragment = CategorizedFeedFragment()
        this.inflateChildFragment(R.id.frame_layout, fragment)
    }

    override fun setUpContentLayout(): Int = R.layout.feed_fragment

    companion object {
        private var feedFragment: FeedFragment? = null

        fun instance(): FeedFragment {

            if (feedFragment == null) {
                feedFragment = FeedFragment()
            }

            return feedFragment!!
        }
    }
}
