package com.meepo.settings

import com.meepo.config.http.ResponseWrapper
import com.meepo.sdk.http.RetrofitManager
import com.meepo.settings.schema.AppUpgrade
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * @author  佟旭
 * @wechat tongxury
 * @date  2020/10/27 11:11 PM
 * @version 1.0
 */
interface IService {

    companion object {
        private var sInstance: IService? = null

        fun get(): IService {
            if (sInstance == null)
                sInstance = RetrofitManager.create(IService::class.java)
            return sInstance as IService
        }
    }


    @GET("/api/v1/app/upgrade")
    fun fetchAppSettings(): Observable<ResponseWrapper<AppUpgrade>>

}