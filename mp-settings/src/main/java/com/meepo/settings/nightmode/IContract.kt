package com.meepo.settings.nightmodeimport com.meepo.sdk.base.mvp.IBaseContract/** * @author  佟旭 * @wechat tongxury * @date  2020/10/14 10:47 PM * @version 1.0 */interface IContract {    interface IView : IBaseContract.IBaseView    interface IPresenter : IBaseContract.IBasePresenter {//        fun saveNightMode(nightMode: String)    }}