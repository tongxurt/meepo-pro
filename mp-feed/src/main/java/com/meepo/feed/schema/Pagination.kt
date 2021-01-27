package com.meepo.feed.schema

data class Pagination(
    var hasMore: Boolean,
    val pageSize: Int,
    val current: Int,
    val total: Int
)