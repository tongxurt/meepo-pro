package com.meepo.pro.main

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View
import com.meepo.pro.R
import com.meepo.sdk.base.mvp.BaseActivity
import com.meepo.sdk.base.mvp.IBaseContract
import kotlinx.android.synthetic.main.main_activity.*

/**
 * @author  佟旭
 * @wechat tongxury
 * @date  2020/10/25 3:08 PM
 * @version 1.0
 */
class MainActivity : BaseActivity<IBaseContract.IBasePresenter>(), IBaseContract.IBaseView {

    override fun setUpContentLayout(): Int = R.layout.main_activity

    override fun setUpPresenter(): IBaseContract.IBasePresenter? = null

    override fun initData() {
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

        normal_tabs.setTabData(
            Store.State.TAB_ENTITIES,
            this,
            R.id.frame_layout,
            Store.State.TAB_ENTITY_FRAGMENTS
        )
    }


    override fun isSupportSwipeBack(): Boolean = false

}