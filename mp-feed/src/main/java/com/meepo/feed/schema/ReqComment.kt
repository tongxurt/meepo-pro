package com.meepo.feed.schema

data class ReqComment(
    val targetId: String,
    val targetType: String,
    val content: String,
    val atCommentId: String?
)