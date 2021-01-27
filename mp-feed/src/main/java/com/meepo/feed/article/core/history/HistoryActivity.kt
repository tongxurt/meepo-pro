package com.meepo.feed.article.core.history

import android.os.Bundle
import android.view.View
import com.meepo.design.helper.StatusBarHelper
import com.meepo.feed.R
import com.meepo.sdk.framework.activity.StaticActivity
import kotlinx.android.synthetic.main.history_activity.*

class HistoryActivity : StaticActivity() {

    override fun initView(view: View, savedInstanceState: Bundle?) {
        StatusBarHelper.immerse(this, header_rl)

        val initialCategoryId = intent.getStringExtra("initialCategoryId")

        back_iv.setOnClickListener { finish() }

        val fragment = FeedHistoryFragment(initialCategoryId)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.frame_layout, fragment)
            .show(fragment)
            .commit()
    }

    override fun setUpContentLayout(): Int = R.layout.history_activity
}