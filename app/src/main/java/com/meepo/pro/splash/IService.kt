package com.meepo.pro.splash

import com.meepo.basic.schema.AppSettings
import com.meepo.pro.splash.Store.REQ_PATH_APP_SETTINGS
import com.meepo.config.http.ResponseWrapper
import com.meepo.sdk.http.RetrofitManager
import io.reactivex.Observable
import retrofit2.http.POST

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


    @POST(REQ_PATH_APP_SETTINGS)
    fun fetchAppSettings(): Observable<ResponseWrapper<AppSettings>>

}