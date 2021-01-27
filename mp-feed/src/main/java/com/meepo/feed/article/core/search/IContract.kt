package com.meepo.feed.article.core.searchimport com.meepo.feed.schema.SearchMetadataimport com.meepo.sdk.base.mvp.IBaseContract/** * @author  佟旭 * @wechat tongxury * @date  2020/10/14 10:47 PM * @version 1.0 */interface IContract {    interface IView : IBaseContract.IBaseView {        fun applySearchMetadata(metadata: SearchMetadata?)        fun applySearchHistory(words: List<String>?)        fun applySearchHistoryChanged()    }    interface IPresenter : IBaseContract.IBasePresenter {        fun getSearchMetadata()        fun getSearchHistory()        fun putSearchHistory(word: String)        fun clearSearchHistory()    }}