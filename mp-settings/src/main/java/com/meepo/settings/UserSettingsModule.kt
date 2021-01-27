package com.meepo.settings

import android.app.Application
import android.content.Context

/**
 * @author  佟旭
 * @wechat tongxury
 * @date  2020/11/4 5:54 PM
 * @version 1.0
 */
object UserSettingsModule {

    private var appContext: Context? = null

    fun init(application: Application) {
        appContext = application.applicationContext

        Store.Func.initNighMode()
    }

    internal fun requireAppContext(): Context {
        if (appContext == null) throw Exception("appContext is not attached! call SettingsManager.init() first!")
        return appContext!!;
    }
}



