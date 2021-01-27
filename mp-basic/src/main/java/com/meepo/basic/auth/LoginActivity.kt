package com.meepo.basic.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import com.meepo.basic.R
import com.meepo.basic.auth.Store.LOGIN_METHOD_CODE
import com.meepo.basic.auth.Store.LOGIN_METHOD_PASSWORD
import com.meepo.basic.auth.Store.LOGIN_PASSWORD_MIN_LENGTH
import com.meepo.design.button.SlickButton
import com.meepo.design.helper.StatusBarHelper
import com.meepo.sdk.base.mvp.BaseActivity
import com.meepo.sdk.base.mvp.IBaseContract
import com.meepo.sdk.helper.ActivityHelper
import com.meepo.basic.auth.code.LoginCodeActivity
import com.meepo.basic.auth.onekey.LoginManager
import com.meepo.basic.schema.UserSummary
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import kotlinx.android.synthetic.main.login_index_activity.*

class LoginActivity : BaseActivity<IContract.IPresenter>(), IContract.IView {

    private var phoneNumberAuthHelper: PhoneNumberAuthHelper? = null

    override fun onCodeSent() {
        ActivityHelper.launchForResult(this, LoginCodeActivity::class.java, 0, "phoneNo", code_phoneNo_et.text.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data?.getSerializableExtra("userSummary")?.let {
            onLoginSuccess(it as UserSummary)
        }

    }

    override fun onLoginSuccess(userSummary: UserSummary) {
        Store.Func.saveUserSummary(userSummary)
        phoneNumberAuthHelper?.quitLoginPage()

        LoginManager.finish()

        finish()
        setResult(1)
    }

    override fun onLoginFail(message: String) {

        Log.e("onLoginFail", message)

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun initData() {
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

        StatusBarHelper.immerse(this, header_rl, true)

        close_iv.setOnClickListener { setResult(0); finish() }

        switchLoginMethod(LOGIN_METHOD_CODE)

        // 登录方式切换
        code_it.setOnClickListener { switchLoginMethod(LOGIN_METHOD_CODE) }
        password_it.setOnClickListener { switchLoginMethod(LOGIN_METHOD_PASSWORD) }
//        one_key_it.setOnClickListener { switchLoginMethod(LOGIN_METHOD_ONE_KEY) }


        // 验证码登录
        switchButton(code_send_btn, false)

        code_phoneNo_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                switchButton(code_send_btn, !text.isNullOrEmpty() && text.length == 11)
            }
        })


        code_send_btn.setOnClickListener {
            mPresenter?.sendAuthCode(code_phoneNo_et.text.toString())
        }

        // 密码登录
        switchButton(password_login_btn, false)

        password_phoneNo_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                switchButton(
                    password_login_btn,
                    !TextUtils.isEmpty(text) && text!!.length == 11 && !TextUtils.isEmpty(password_pw_et.text) && password_pw_et.text!!.length >= LOGIN_PASSWORD_MIN_LENGTH
                )
            }
        })

        password_pw_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                switchButton(
                    password_login_btn,
                    !TextUtils.isEmpty(text) && text!!.length >= LOGIN_PASSWORD_MIN_LENGTH && !TextUtils.isEmpty(password_phoneNo_et.text) && password_phoneNo_et.text!!.length == 11
                )
            }
        })

        // 密码显隐切换
        password_show_iv.setOnClickListener {
            val isShow = password_pw_et.inputType == InputType.TYPE_CLASS_TEXT
            password_pw_et.transformationMethod =
                if (isShow) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()

            password_pw_et.inputType = if (isShow) InputType.TYPE_TEXT_VARIATION_PASSWORD else InputType.TYPE_CLASS_TEXT
            password_show_iv.setImageResource(if (isShow) R.drawable.ic_eye else R.drawable.ic_eye_close)
        }

        password_login_btn.setOnClickListener {

            password_phoneNo_et.requestFocus()
            mPresenter?.loginByPassword(password_phoneNo_et.text.toString(), password_pw_et.text.toString())
        }
    }

    private fun switchButton(button: SlickButton, isEnabled: Boolean) {
        button.isEnabled = isEnabled
        button.alpha = if (isEnabled) 1f else 0.5f
    }


    private fun switchLoginMethod(method: String) {

        code_ll.visibility = View.GONE
        password_ll.visibility = View.GONE

        when (method) {
            LOGIN_METHOD_CODE -> {
                code_ll.visibility = View.VISIBLE
            }
            LOGIN_METHOD_PASSWORD -> {
                password_ll.visibility = View.VISIBLE
            }
        }

        switchLoginMethodIcon(method)
    }


    private fun switchLoginMethodIcon(method: String) {

        method_code_ll.visibility = View.VISIBLE
        method_password_ll.visibility = View.VISIBLE

        when (method) {
            LOGIN_METHOD_CODE -> {
                method_code_ll.visibility = View.GONE
            }
            LOGIN_METHOD_PASSWORD -> {
                method_password_ll.visibility = View.GONE
            }
        }

    }


    override fun setUpContentLayout(): Int = R.layout.login_index_activity

    override fun setUpPresenter(): IBaseContract.IBasePresenter? = Presenter()

    override fun isSupportSwipeBack(): Boolean = false
}