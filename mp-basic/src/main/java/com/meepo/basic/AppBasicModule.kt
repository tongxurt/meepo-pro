package com.meepo.basic

import android.app.Activity
import android.content.Context
import com.meepo.basic.auth.Store
import com.meepo.basic.auth.onekey.LoginManager
import com.meepo.basic.schema.AppSettings
import com.meepo.basic.schema.UserSummary
import com.meepo.basic.settings.State
import com.meepo.basic.umeng.UMengManager

/**
 * @author  佟旭
 * @wechat tongxury
 * @date  2020/11/8 12:07 AM
 * @version 1.0
 */
object AppBasicModule {

    private lateinit var appContext: Context


    fun getSettings(): AppSettings {
        return State.appSettings
    }

    fun logout() {
        Store.Func.clearUserSummary()
    }

    fun login(activity: Activity) {
        LoginManager.start(activity)
    }

    fun getUserSummary(): UserSummary? {
        return Store.Func.getUserSummary()
    }

    fun init(context: Context) {
        appContext = context

        State.update()

        UMengManager.init(context)

    }

    internal fun requireAppContext(): Context {
        return appContext
    }
}
