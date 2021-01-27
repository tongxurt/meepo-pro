package com.meepo.feed.minivideo.profile

import android.os.Bundle
import android.view.View
import com.meepo.design.helper.StatusBarHelper
import com.meepo.feed.R
import com.meepo.feed.Store
import com.meepo.feed.schema.FeedResult
import com.meepo.sdk.framework.BaseActivity
import com.meepo.sdk.framework.IPresenter
import kotlinx.android.synthetic.main.video_mini_profile_activity.*

class ProfileActivity : BaseActivity<Presenter>(), IContract.IFeedListView {
    override fun refresh(rsp: FeedResult?) {
    }

    override fun append(rsp: FeedResult?) {
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

        StatusBarHelper.immerse(this, header_rl)

        back_iv.setOnClickListener { finish() }
    }

    override fun loadData() {
    }

    override fun setUpContentLayout(): Int = R.layout.video_mini_profile_activity

    override fun setUpPresenter(): IPresenter? = Presenter()
}