package com.meepo.feed.article.core.search

import com.meepo.feed.FeedFlowModule
import com.meepo.feed.Store
import com.meepo.sdk.store.persistence.sp.SharedPrefsFactory

object SearchStore {

    object Func {
        fun fetchSearchHistory(): List<String>? {
            return SharedPrefsFactory.setUp(FeedFlowModule.requireAppContext())
                .getStringSet(Store.STORE_SEARCH_KEY)?.toMutableList()?.reversed()
        }

        fun putSearchHistory(word: String) {
            SharedPrefsFactory.setUp(FeedFlowModule.requireAppContext())
                .upsertString(Store.STORE_SEARCH_KEY, word)
        }

        fun clearSearchHistory() {
            SharedPrefsFactory.setUp(FeedFlowModule.requireAppContext())
                .remove(Store.STORE_SEARCH_KEY)
        }
    }
}