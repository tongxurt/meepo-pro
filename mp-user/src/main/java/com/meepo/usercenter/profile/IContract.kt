package com.meepo.usercenter.profileimport com.meepo.basic.schema.UserSummaryimport com.meepo.sdk.framework.IPresenterimport com.meepo.sdk.framework.IView/** * @author  佟旭 * @wechat tongxury * @date  2020/10/14 10:47 PM * @version 1.0 */interface IContract {    interface IProfileView : IView {        fun applyUserSummary(userSummary: UserSummary?)    }    interface IProfilePresenter : IPresenter {        fun getUserSummary()    }}