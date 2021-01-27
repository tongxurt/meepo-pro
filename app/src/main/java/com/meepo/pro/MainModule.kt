package com.meepo.pro

import android.app.Application
import android.content.Context
import com.meepo.basic.AppBasicModule
import com.meepo.sdk.helper.DeviceHelper
import com.meepo.sdk.helper.DisplayHelper
import com.meepo.sdk.http.BaseParams
import com.meepo.sdk.network.NetworkHelper
import com.meepo.usercenter.UserCenterModule

object MainModule {

    fun addHttpBaseParamsFunc(): BaseParams {
        return BaseParams(
            token = AppBasicModule.getUserSummary()?.token,
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE.toString(),
            source = BuildConfig.FLAVOR,
            packageName = BuildConfig.APPLICATION_ID,
            userAgent = DeviceHelper.getUserAgent(),
            ip = "",
            mac = "",
            imei = "",
            imsi = "",
            androidId = "",
            brand = DeviceHelper.getBrand(),
            model = DeviceHelper.getModel(),
            sdkVc = DeviceHelper.getSdkVersion(),
            osVersion = DeviceHelper.getSystemVersion(),
            height = DisplayHelper.getScreenHeight(requireAppContext()),
            width = DisplayHelper.getScreenWidth(requireAppContext()),
            network = NetworkHelper.networkType,
            language = DeviceHelper.getLanguage(),
            extras = mapOf()
        )
    }

    private var appContext: Context? = null

    fun init(application: Application) {
        appContext = application.applicationContext
    }

    internal fun requireAppContext(): Context {
        if (appContext == null) throw Exception("appContext is not attached! call Module.init() first!")
        return appContext!!;
    }
}

