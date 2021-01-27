package com.meepo.basic.auth.code

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.meepo.basic.R
import com.meepo.basic.auth.Store.RESEND_CODE_DURATION
import com.meepo.basic.schema.UserSummary
import com.meepo.design.helper.StatusBarHelper
import com.meepo.sdk.base.mvp.BaseActivity
import com.meepo.sdk.base.mvp.IBaseContract
import com.meepo.sdk.countdown.Countdown
import com.meepo.sdk.countdown.OnChangeListener
import com.meepo.sdk.helper.ActivityHelper
import kotlinx.android.synthetic.main.login_code_activity.*
import kotlinx.android.synthetic.main.login_code_activity.close_iv
import kotlinx.android.synthetic.main.login_code_activity.header_rl

class LoginCodeActivity : BaseActivity<Presenter>(), IContract.IView {
    override fun onLoginSuccess(userSummary: UserSummary) {

        val intent = Intent()
        intent.putExtra("userSummary", userSummary)
        setResult(0, intent)

        finish()
    }

    override fun initData() {
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        StatusBarHelper.immerse(this, header_rl, true)

        val phoneNo = ActivityHelper.getStringExtra(this.intent, "phoneNo")!!

        close_iv.setOnClickListener { finish() }
//        switchLoginButton(false)


        code_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                switchLoginButton(!p0.isNullOrEmpty() && p0.length == 4)
            }
        })

        phone_tv.text = phoneNo

        resend_tv.setOnClickListener {
            mPresenter?.sendAuthCode(phoneNo)
        }

        Countdown(60, object : OnChangeListener {
            override fun onComplete() {
                resend_tv.text = "重新发送"
                resend_tv.setTextColor(resources.getColor(R.color.sub_text_color))
                resend_tv.isEnabled = true
            }

            @SuppressLint("SetTextI18n")
            override fun onNext(p0: Long?) {
                resend_tv.text = "重新发送(${p0})"
            }

            override fun onError(p0: Throwable?) {
            }

            @SuppressLint("SetTextI18n")
            override fun onStart() {
                resend_tv.setTextColor(resources.getColor(R.color.hint_text_color))
                resend_tv.isEnabled = false
                resend_tv.text = "重新发送(${RESEND_CODE_DURATION})"
            }
        }).start()


        login_btn.setOnClickListener {
            mPresenter?.loginByCode(phoneNo, code_et.text.toString())
        }
    }


    private fun switchLoginButton(isEnabled: Boolean) {
        login_btn.isEnabled = isEnabled
        login_btn.alpha = if (isEnabled) 1.0f else 0.5f
    }

    override fun setUpContentLayout(): Int = R.layout.login_code_activity

    override fun setUpPresenter(): IBaseContract.IBasePresenter? = Presenter()
}