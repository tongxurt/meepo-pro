package com.meepo.feed.minivideoimport com.meepo.feed.schema.FeedResultimport com.meepo.sdk.framework.IPresenterimport com.meepo.sdk.framework.IView/** * @author  佟旭 * @wechat tongxury * @date  2020/10/14 10:47 PM * @version 1.0 */interface IContract {    interface IFeedListView : IView {        fun refresh(rsp: FeedResult?)        fun append(rsp: FeedResult?)    }    interface IFeedListPresenter : IPresenter {        fun refresh(category: String)        fun fetchMore(category: String)        fun remove(id: String)    }}