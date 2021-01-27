package com.meepo.campaign

import android.app.Application
import android.content.Context

object CampaignModule {

    private var appContext: Context? = null

    fun init(application: Application) {
        appContext = application.applicationContext
    }

    internal fun requireAppContext(): Context {
        if (appContext == null) throw Exception("appContext is not attached! call Module.init() first!")
        return appContext!!;
    }
}