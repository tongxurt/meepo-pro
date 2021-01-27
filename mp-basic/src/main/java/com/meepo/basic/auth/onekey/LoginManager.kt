package com.meepo.basic.auth.onekey

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import com.meepo.basic.R
import com.meepo.sdk.observer.Observer
import com.meepo.basic.auth.LoginActivity
import com.meepo.basic.auth.Service
import com.meepo.basic.auth.Store
import com.meepo.basic.schema.UserSummary
import com.meepo.config.Config.ONE_KEY_AUTH_SECRET
import com.meepo.sdk.observer.ObserverManager.applySchedulers
import com.mobile.auth.gatewayauth.*
import com.mobile.auth.gatewayauth.ui.AbstractPnsViewDelegate
import org.json.JSONObject

internal object LoginManager {
    private lateinit var phoneNumberAuthHelper: PhoneNumberAuthHelper

    fun finish() {
        phoneNumberAuthHelper.quitLoginPage()
    }

    fun start(activity: Activity) {

        phoneNumberAuthHelper = PhoneNumberAuthHelper.getInstance(activity, object : TokenResultListener {
            override fun onTokenFailed(p0: String?) {
                phoneNumberAuthHelper.hideLoginLoading()
            }

            override fun onTokenSuccess(p0: String?) {
                phoneNumberAuthHelper.hideLoginLoading()

                p0?.let {
                    val accessToken = JSONObject(it).getString("token")
                    Service.get().loginByOneKey(accessToken)
                        .applySchedulers()
                        .map { rsp -> rsp.data }
//                        .compose(mView?.bindToLife<UserSummary>())
//                        .doFinally { mView?.doFinally() }
                        .subscribe(object : Observer<UserSummary>() {
                            override fun onSuccess(t: UserSummary?) {
                                t?.let {
                                    Store.Func.saveUserSummary(it)
                                    phoneNumberAuthHelper!!.quitLoginPage()
                                }
                            }

                            override fun onFailure(e: Throwable) {
                                Log.e("onFailure", e.message)
                            }
                        })
                }
            }
        })

        phoneNumberAuthHelper.reporter.setLoggerEnable(true)

        phoneNumberAuthHelper.setAuthSDKInfo(ONE_KEY_AUTH_SECRET)


        phoneNumberAuthHelper.removeAuthRegisterXmlConfig()
        phoneNumberAuthHelper.removeAuthRegisterViewConfig()

        phoneNumberAuthHelper.addAuthRegisterXmlConfig(
            AuthRegisterXmlConfig.Builder()
                .setLayout(R.layout.login_one_key_layout, object : AbstractPnsViewDelegate() {
                    override fun onViewCreated(p0: View?) {
//                        StatusBarHelper.immerse(p0.context as Activity, findViewById(R.id.header_rl), true)

//                        StatusBarHelper.setMargin(context, findViewById(R.id.header_fl))

                        findViewById(R.id.close_iv).setOnClickListener {
                            phoneNumberAuthHelper.quitLoginPage()
                        }

                        findViewById(R.id.switch_tv)?.setOnClickListener {
//                            Toast.makeText(activity, "切换到短信登录方式1", Toast.LENGTH_SHORT).show()

//                            phoneNumberAuthHelper.quitLoginPage()

                            val pIntent = Intent(activity, LoginActivity::class.java)
                            activity.startActivity(pIntent)
                        }


                    }
                })
                .build()
        )


        // 切换登录方式
//        phoneNumberAuthHelper.addAuthRegistViewConfig(
//            "switch_msg", AuthRegisterViewConfig.Builder()
//                .setView(initSwitchView(activity))
//                .setRootViewId(AuthRegisterViewConfig.RootViewId.ROOT_VIEW_ID_BODY)
//                .setCustomInterface {
//                    Toast.makeText(activity, "切换到短信登录方式", Toast.LENGTH_SHORT).show()
////                    val pIntent = Intent(activity, LoginActivity::class.java)
////                    activity.startActivityForResult(pIntent, 1002)
//
////                    phoneNumberAuthHelper.quitLoginPage()
//                }
//                .build()
//        )

//        phoneNumberAuthHelper.setActivityResultListener { reqCode, resCode, intent ->
//            Log.e("ActivityResultListener", "" + resCode)
//
//
//            if (reqCode == 1) {
//                if (resCode == 1) {
//                    phoneNumberAuthHelper.quitLoginPage()
//                }
//            }
//        }


        phoneNumberAuthHelper.setAuthUIConfig(
            AuthUIConfig.Builder()
                .setLogBtnText("手机号码一键登录")
                .setAppPrivacyOne("「用户协议」", "https://www.baidu.com")
                .setAppPrivacyTwo("「隐私政策」", "https://www.baidu.com")
                .setNavHidden(true)
                .setLogoHidden(false)
                .setLogoImgPath("app_logo")
//                .setLogoOffsetY(0)
                .setLogoWidth(90)
                .setLogoHeight(90)
                .setPrivacyTextSize(10)
                .setSloganHidden(true)
                .setSwitchAccHidden(true)
                .setAuthPageActIn("slide_in_bottom", "slide_out_bottom")
                .setAuthPageActOut("slide_out_bottom", "slide_out_bottom")
                .setPrivacyState(false)
                .setCheckboxHidden(true)
                .setLightColor(true)
                .setStatusBarColor(Color.TRANSPARENT)
                .setStatusBarUIFlag(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                .setPageBackgroundPath("default_background")
//                .setDialogHeight(500)
//                .setDialogBottom(true)

                .create()
        )


        phoneNumberAuthHelper.getLoginToken(activity, 5000)
    }


}