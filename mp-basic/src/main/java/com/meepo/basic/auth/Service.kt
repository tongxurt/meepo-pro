package com.meepo.basic.auth

import com.meepo.basic.schema.UserSummary
import com.meepo.config.http.ResponseWrapper
import com.meepo.sdk.http.RetrofitManager
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query

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
                sInstance = RetrofitManager.create(
                    Service::class.java, null
                )
            return sInstance as Service
        }
    }


    @POST("/api/v1/auth/sms_codes")
    fun sendCode(@Query("phoneNo") phoneNo: String): Observable<ResponseWrapper<Unit>>

    @POST("/api/v1/auth/sms-login")
    fun loginByCode(@Query("phoneNo") phoneNo: String, @Query("code") code: String): Observable<ResponseWrapper<UserSummary>>

    @POST("/api/v1/auth/pw-login")
    fun loginByPassword(@Query("phoneNo") phoneNo: String, @Query("password") password: String): Observable<ResponseWrapper<UserSummary>>

    @POST("/api/v1/auth/one-key-login")
    fun loginByOneKey(@Query("accessToken") accessToken: String): Observable<ResponseWrapper<UserSummary>>
}