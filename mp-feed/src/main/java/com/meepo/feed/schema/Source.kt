package com.meepo.feed.schema

import java.io.Serializable

data class Source(
    val id: String = "",
    val name: String = "",
    val avatar: String? = "",
    val desc: String? = "",
    val followed: Boolean?
) : Serializable