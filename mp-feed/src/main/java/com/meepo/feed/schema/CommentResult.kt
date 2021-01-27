package com.meepo.feed.schema

import com.meepo.feed.Store

data class CommentResult(
    val items: List<Comment>?,
    val pagination: Pagination,
    val key: String
)
