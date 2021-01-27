package com.meepo.feed.schema

import com.meepo.basic.schema.UserSummary
import com.meepo.feed.Store
import java.io.Serializable

data class Item(
    val id: String,
    val title: String = "",
    val desc: String?,
    val covers: List<Resource>? = arrayListOf(),
    val source: Source?,
    val style: Style?,
    val content: Resource?,
    val supplements: List<Supplement>?,
    val h5: Resource?,
    val itemStat: Map<String, ItemStatByType>?,
    val author: UserSummary?
) : Serializable {

    data class Supplement(
        val value: String
    ) : Serializable

    data class ItemStatByType(
        val count: Int,
        val containsSelf: Boolean
    ) : Serializable

    data class Style(
        val viewType: Int
    ) : Serializable

    fun findOneCover(): String {
        covers?.let {
            if (it.isNotEmpty()) {
                return it[0].content ?: ""
            }
        }
        return ""
    }

    fun isCollected(): Boolean {
        return isUserItem(Store.USER_ITEM_TYPE_COLLECTED)
    }

    fun isUserItem(userItemType: String): Boolean {
        itemStat?.let {
            it[userItemType]?.let { stat ->
                return stat.containsSelf
            }
        }

        return false
    }

    override fun equals(other: Any?): Boolean {
        return other != null && other is Item && other.id == this.id
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }


}