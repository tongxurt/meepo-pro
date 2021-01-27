package com.meepo.pro.main

import androidx.fragment.app.Fragment
import com.meepo.design.tabs.listener.ITabItem
import com.meepo.feed.article.pages.FeedFragment
import com.meepo.feed.minivideo.MiniVideoFragment
import com.meepo.feed.shortvideo.ShortVideoFragment
import com.meepo.feed.video.VideoFragment
import com.meepo.pro.R
import com.meepo.taskcenter.task.TaskFragment
import com.meepo.usercenter.profile.UserProfileFragment

object Store {

    object State {

        internal val TAB_ENTITIES: ArrayList<ITabItem> = arrayListOf(
            Schema.TabEntity("小视频", R.mipmap.ic_tabs_home_selected, R.mipmap.ic_tabs_home),
            Schema.TabEntity("短视频", R.mipmap.ic_tabs_message_selected, R.mipmap.ic_tabs_message),
            Schema.TabEntity("资讯", R.mipmap.ic_tabs_discovery_selected, R.mipmap.ic_tabs_discovery),
            Schema.TabEntity("我的", R.mipmap.ic_tabs_me_selected, R.mipmap.ic_tabs_me)
        )

        internal val TAB_ENTITY_FRAGMENTS: ArrayList<Fragment> = arrayListOf(
//            FeedListFragment(Category("yule", "娱乐")),
//            FeedFragment(),
//            CategorizedFeedFragment(),
            MiniVideoFragment.instance(),
            ShortVideoFragment.instance(),
            FeedFragment.instance(),
            UserProfileFragment.instance()
        )

    }

    object Schema {
        class TabEntity(
            val title: String,
            val selectedIcon: Int,
            val unSelectedIcon: Int
        ) : ITabItem {

            override fun getTabTitle(): String {
                return title
            }

            override fun getTabUnselectedIcon(): Int {
                return unSelectedIcon
            }

            override fun getTabSelectedIcon(): Int {
                return selectedIcon
            }
        }
    }

    object Func {

    }
}