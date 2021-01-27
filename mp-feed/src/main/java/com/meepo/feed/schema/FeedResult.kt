package com.meepo.feed.schema


data class FeedResult(
    val key: String,
    val pagination: Pagination,
    val items: List<Item>?
)