package com.meepo.feed.schema

import com.meepo.basic.schema.UserSummary
import java.io.Serializable

data class Comment(
    val id: String,
    val targetId: String,
    val targetType: String,
    val user: UserSummary?,
    val content: String?,
    val createdAt: String?,
    val stat: Stat?,
    val tags: List<Tag>?,
    val at: At?
) : Serializable {
    data class Stat(
        val liked: Boolean?,
        val likeCount: Int?,
        val replyCount: Int?
    )

    data class At(
        val user: UserSummary,
        val content: String
    )
}
