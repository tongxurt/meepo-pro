package com.meepo.feed

import com.meepo.feed.FeedFlowModule.requireAppContext
import com.meepo.feed.schema.Category
import com.meepo.feed.schema.FeedResult
import com.meepo.feed.schema.Item
import com.meepo.feed.schema.Pagination
import com.meepo.sdk.store.persistence.sp.SharedPrefsFactory

object Store {

    const val COMMENT_TARGET_ITEM = "item"
    const val COMMENT_TARGET_COMMENT = "comment"

    const val REFRESH_DELAY = 0;
    const val LOAD_MORE_DELAY = 0;

    const val MEDIA_TYPE_IMAGE = "image"
    const val MEDIA_TYPE_VIDEO = "video"
    const val MEDIA_TYPE_HTML = "html"

    const val IMAGE_VIEW_TYPE_SINGLE_IMAGE = 1
    const val IMAGE_VIEW_TYPE_GROUP_IMAGE = 3
    const val IMAGE_VIEW_TYPE_LARGE_IMAGE = 2

    const val STORE_SEARCH_KEY = "searchHistoryKey"
    const val STORE_READ_HISTORY_KEY = "readHistoryKey"

    const val USER_ITEM_TYPE_READ = "read"
    const val USER_ITEM_TYPE_CERTIFIED = "certified"
    const val USER_ITEM_TYPE_DISAGREED = "disagreed"
    const val USER_ITEM_TYPE_COLLECTED = "collected"
    const val USER_ITEM_TYPE_PUBLISHED = "published"
    const val USER_ITEM_TYPE_SHARED = "shared"
    const val USER_ITEM_TYPE_COMMENTED = "commented"

    const val ACTION_ADD = 0
    const val ACTION_DELETE = 1

    internal object State {
        var historyTabs = mutableListOf(
            Category(id = USER_ITEM_TYPE_READ, name = "历史"),
            Category(id = USER_ITEM_TYPE_COMMENTED, name = "评论"),
            Category(id = USER_ITEM_TYPE_COLLECTED, name = "收藏"),
            Category(id = USER_ITEM_TYPE_SHARED, name = "分享")
        )
    }


    internal object Func {
        fun saveReadHistory(item: Item) {
            SharedPrefsFactory.setUp(requireAppContext()).upsertObject(STORE_READ_HISTORY_KEY, item, Item::class.java)
        }

        fun getReadHistory(): FeedResult? {
            val list =
                SharedPrefsFactory.setUp(requireAppContext()).getObjectSet(STORE_READ_HISTORY_KEY, Item::class.java)?.reversed()?.toList()

            var total = 0
            list?.let { total = list.size }

            return FeedResult(
                key = "",
                pagination = Pagination(hasMore = false, pageSize = 1, current = 1, total = total),
                items = list
            )
        }
    }

}