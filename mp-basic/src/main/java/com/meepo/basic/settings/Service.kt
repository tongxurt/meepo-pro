package com.meepo.basic.settings

import com.meepo.basic.schema.AppSettings
import com.meepo.config.http.ResponseWrapper
import com.meepo.sdk.http.RetrofitManager
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * @author  佟旭
 * @wechat tongxury
 * @date  2020/10/27 11:11 PM
 * @version 1.0
 */
internal interface Service {

    companion object {
        private var sInstance: Service? = null

        fun get(): Service {
            if (sInstance == null)
                sInstance = RetrofitManager.create(Service::class.java)
            return sInstance as Service
        }
    }


    @GET("/api/v1/app/settings")
    fun fetchAppSettings(): Observable<ResponseWrapper<AppSettings>>

}