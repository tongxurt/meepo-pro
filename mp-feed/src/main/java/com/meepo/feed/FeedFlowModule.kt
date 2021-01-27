package com.meepo.feed

import android.app.Application
import android.content.Context

/**
 * @author  佟旭
 * @wechat tongxury
 * @date  2020/11/8 6:36 PM
 * @version 1.0
 */
object FeedFlowModule {

    private var appContext: Context? = null

    fun init(application: Application) {
        appContext = application.applicationContext
    }

    internal fun requireAppContext(): Context {
        if (appContext == null) throw Exception("appContext is not attached! call Module.init() first!")
        return appContext!!;
    }
}