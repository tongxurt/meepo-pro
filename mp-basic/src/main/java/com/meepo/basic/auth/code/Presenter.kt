package com.meepo.basic.auth.codeimport android.util.Logimport com.meepo.basic.auth.Serviceimport com.meepo.basic.schema.UserSummaryimport com.meepo.sdk.base.mvp.BasePresenterimport com.meepo.sdk.observer.Observerimport com.meepo.sdk.observer.ObserverManager.applySchedulers/** * @author  佟旭 * @wechat tongxury * @date  2020/10/14 10:46 PM * @version 1.0 */class Presenter : BasePresenter<IContract.IView>(), IContract.IPresenter {    override fun sendAuthCode(phoneNo: String) {        Service.get().sendCode(phoneNo)            .applySchedulers()            .map { rsp -> rsp.data }            .compose(mView?.bindToLife<Unit>())            .doFinally { mView?.doFinally() }            .subscribe(object : Observer<Unit>() {                override fun onSuccess(t: Unit?) {                }                override fun onFailure(e: Throwable) {                    Log.e("onFailure", e.message)                }            })    }    override fun loginByCode(phoneNo: String, code: String) {        Service.get().loginByCode(phoneNo, code)            .applySchedulers()            .map { rsp -> rsp.data }            .compose(mView?.bindToLife<UserSummary>())            .doFinally { mView?.doFinally() }            .subscribe(object : Observer<UserSummary>() {                override fun onSuccess(t: UserSummary?) {                    t?.let {                        mView?.onLoginSuccess(it)                    }                }                override fun onFailure(e: Throwable) {                    Log.e("onFailure", e.message)                }            })    }}