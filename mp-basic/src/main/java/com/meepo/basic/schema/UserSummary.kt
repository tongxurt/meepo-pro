package com.meepo.basic.schema

import java.io.Serializable

data class UserSummary(
    val id: String,
    val nickname: String? ,
    val avatar: String?,
    val phone: String?,
    val sign: String?,
    val token: String?,
    val tags: List<String>?
) : Serializable