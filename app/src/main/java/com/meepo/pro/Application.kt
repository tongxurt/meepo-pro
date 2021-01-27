package com.meepo.pro

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import com.meepo.campaign.CampaignModule
import com.meepo.feed.FeedFlowModule
import com.meepo.feed.minivideo.MiniVideoFragment
import com.meepo.feed.minivideo.profile.ProfileActivity
import com.meepo.sdk.MeepoSDK
import com.meepo.settings.UserSettingsModule
import com.meepo.taskcenter.TaskCenterModule
import com.meepo.usercenter.UserCenterModule

/**
 * @author  佟旭
 * @wechat tongxury
 * @date  2020/10/25 2:58 PM
 * @version 1.0
 */
class Application : Application() {

//    override fun attachBaseContext(base: Context?) {
//        super.attachBaseContext(base)
//        MultiDex.install(this)
//    }

    override fun onCreate() {
        super.onCreate()

        UserSettingsModule.init(this)
        UserCenterModule.init(this)
        FeedFlowModule.init(this)
        TaskCenterModule.init(this)
        MainModule.init(this)
        // SDK 初始化 todo
        MeepoSDK.init(this)
        CampaignModule.init(this)

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())

        this.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityPaused(p0: Activity) {
                // 理论上只有 className 是 MainActivity 需要这个回调
                if (p0.componentName.className == ProfileActivity::class.java.simpleName) {
                    return
                }
                MiniVideoFragment.instance().pauseCurrentIfExists()

            }

            override fun onActivityStarted(p0: Activity) {
                Log.e("app Lifecycle", "onActivityStarted" + p0.componentName.className)
            }

            override fun onActivityDestroyed(p0: Activity) {
                Log.e("app Lifecycle", "onActivityDestroyed" + p0.componentName.className)
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
                Log.e("app Lifecycle", "onActivitySaveInstanceState" + p0.componentName.className)
            }

            override fun onActivityStopped(p0: Activity) {
                Log.e("app Lifecycle", "onActivityStopped" + p0.componentName.className)
            }

            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                Log.e("app Lifecycle", "onActivityCreated" + p0.componentName.className)
            }

            override fun onActivityResumed(p0: Activity) {

                if (p0.componentName.className == ProfileActivity::class.java.canonicalName) {
                    return
                }

                MiniVideoFragment.instance().resumeCurrentIfExistsAndVisible()

            }
        })
    }

}