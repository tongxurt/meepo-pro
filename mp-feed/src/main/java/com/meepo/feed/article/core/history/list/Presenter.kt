package com.meepo.feed.article.core.history.listimport android.util.Logimport com.meepo.feed.Serviceimport com.meepo.feed.Storeimport com.meepo.feed.schema.FeedResultimport com.meepo.sdk.framework.BasePresenterimport com.meepo.sdk.observer.Observerimport com.meepo.sdk.observer.ObserverManagerimport com.meepo.sdk.observer.ObserverManager.applyObserverimport com.meepo.sdk.observer.ObserverManager.applySchedulers/** * @author  佟旭 * @wechat tongxury * @date  2020/10/14 10:46 PM * @version 1.0 */class Presenter : BasePresenter<IContract.IHistoryView>(), IContract.IHistoryPresenter {    private var pageMap = hashMapOf<String, Int>()    override fun fetchMore(itemType: String) {        val page: Int = if (pageMap[itemType] != null) pageMap[itemType]!! else 1        if (itemType == Store.USER_ITEM_TYPE_READ) {            ObserverManager                .applyFunc { Store.Func.getReadHistory() }                .compose(mView?.bindToLife<FeedResult>())                .applyObserver(object : Observer<FeedResult>() {                    override fun onSuccess(t: FeedResult?) {                        mView?.append(t, page)                    }                    override fun onFailure(e: Throwable) {                        Log.e("Presenter 3", e.message)                    }                })        } else {            Service.get().fetchUserItems(itemType, page)                .applySchedulers()                .map { rsp -> rsp.data }                .compose(mView?.bindToLife<FeedResult>())                .subscribe(object : Observer<FeedResult>() {                    override fun onSuccess(t: FeedResult?) {                        mView?.append(t, page)                        pageMap[itemType] = page + 1                    }                    override fun onFailure(e: Throwable) {                        Log.e("onFailure", e.message)                    }                })        }    }}