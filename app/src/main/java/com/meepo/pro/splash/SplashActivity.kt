package com.meepo.pro.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.meepo.basic.AppBasicModule
import com.meepo.pro.BuildConfig
import com.meepo.pro.MainModule
import com.meepo.pro.R
import com.meepo.pro.main.MainActivity
import com.meepo.sdk.MeepoSDK
import com.meepo.sdk.base.mvp.BaseActivity
import com.meepo.sdk.base.mvp.IBaseContract
import com.meepo.sdk.countdown.Countdown
import com.meepo.sdk.countdown.OnChangeListener
import com.meepo.sdk.helper.ActivityHelper
import com.meepo.sdk.http.HttpConfig
import kotlinx.android.synthetic.main.splash_activity.*

/**
 * @author  佟旭
 * @wechat tongxury
 * @date  2020/10/25 3:08 PM
 * @version 1.0
 */
class SplashActivity : BaseActivity<Presenter>(), IContract.IView {

    override fun setUpContentLayout(): Int = R.layout.splash_activity

    override fun setUpPresenter(): IBaseContract.IBasePresenter? = Presenter()

    override fun initData() {

        MeepoSDK.initRequireActivity(HttpConfig(
            defaultBaseUrl = BuildConfig.api_host,
            hmacSalt = "a922d12919a9068cf25611ab524ccd41",
            mock = false,
            addBaseParamsFunc = { MainModule.addHttpBaseParamsFunc() },
            httpCodeHandler = { code -> if (code == 401) AppBasicModule.login(this) }
        ))

        AppBasicModule.init(this)
    }

    @SuppressLint("SetTextI18n")
    override fun initView(view: View, savedInstanceState: Bundle?) {
        val countdown = Countdown(5, object : OnChangeListener {
            override fun onComplete() {
                toMain()
            }

            override fun onNext(p0: Long?) {
                tag.text = "跳过 ${p0}"
            }

            override fun onError(p0: Throwable?) {
            }

            override fun onStart() {
                tag.text = "跳过 ${5}"
            }
        }).start()


        tag.setOnClickListener {
            countdown?.shutdown()
            toMain()
        }
    }

    private fun toMain() {
        ActivityHelper.launch(this, MainActivity::class.java)
        finish()
    }

}