package com.meepo.usercenter.profile

import android.os.Bundle
import android.view.View
import com.meepo.basic.AppBasicModule
import com.meepo.design.helper.ImageLoader
import com.meepo.design.helper.StatusBarHelper
import com.meepo.design.settings.SettingsItem
import com.meepo.feed.Store.USER_ITEM_TYPE_READ
import com.meepo.feed.article.core.history.HistoryActivity
import com.meepo.sdk.framework.IPresenter
import com.meepo.sdk.framework.fragment.RefreshOnVisibleFragment
import com.meepo.sdk.helper.ActivityHelper
import com.meepo.settings.SettingsActivity
import com.meepo.usercenter.R
import com.meepo.usercenter.Store.SETTINGS_ITEM_SETTINGS
import com.meepo.basic.schema.UserSummary
import com.meepo.feed.Store.USER_ITEM_TYPE_COLLECTED
import kotlinx.android.synthetic.main.profile_fragment.*

/**
 * @author  佟旭
 * @wechat tongxury
 * @date  2020/10/29 8:29 AM
 * @version 1.0
 */
class UserProfileFragment private constructor() : RefreshOnVisibleFragment<Presenter>(), IContract.IProfileView {

    override fun applyUserSummary(userSummary: UserSummary?) {

        if (userSummary != null) {
            ImageLoader.loadImage(this.requireContext(), userSummary.avatar, avatar_ci)
            username_tv.text = userSummary.nickname
            desc_tv.text = userSummary.sign
        } else {

        }
    }

    override fun loadData() {
        mPresenter?.getUserSummary()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

        StatusBarHelper.immerse(this.requireActivity(), header_ll)

        settings_iv.setOnClickListener {
            ActivityHelper.launch(this.requireActivity(), SettingsActivity::class.java)
        }


//        context_nsv.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
//            Log.e("NScrollView", "" + scrollX + " " + scrollY + " " + oldScrollX + " " + oldScrollY)
//            divider.visibility = if (scrollY == 0) View.GONE else View.VISIBLE
//        }

        collect_it.setOnClickListener {
            ActivityHelper.launch(this.requireActivity(), HistoryActivity::class.java, "initialCategoryId", USER_ITEM_TYPE_COLLECTED)
        }

        history_it.setOnClickListener {
            ActivityHelper.launch(this.requireActivity(), HistoryActivity::class.java, "initialCategoryId", USER_ITEM_TYPE_READ)
        }

        settings_view.setSettingsItems(
            mutableListOf(
                SettingsItem("key1", "title1", "", ""),
                SettingsItem("key2", "title2", "", ""),
                SettingsItem("logout", "Logout", "", ""),
                SettingsItem("login", "Login", "", ""),
                SettingsItem(SETTINGS_ITEM_SETTINGS, "设置", "", "")

            )
        )

        settings_view.setOnItemClickListener { settingsItem, i ->
            when (settingsItem.key) {
                SETTINGS_ITEM_SETTINGS -> ActivityHelper.launch(this.requireContext(), SettingsActivity::class.java)
                "login" -> AppBasicModule.login(requireActivity())
                "logout" -> AppBasicModule.logout()
                else -> ActivityHelper.launch(this.requireActivity(), HistoryActivity::class.java, "initialCategoryId", USER_ITEM_TYPE_READ)
            }
        }
    }


    override fun setUpContentLayout(): Int = R.layout.profile_fragment
    override fun setUpPresenter(): IPresenter? = Presenter()

    companion object {
        private var userProfileFragment: UserProfileFragment? = null

        fun instance(): UserProfileFragment {

            if (userProfileFragment == null) {
                userProfileFragment = UserProfileFragment()
            }

            return userProfileFragment!!
        }
    }
}