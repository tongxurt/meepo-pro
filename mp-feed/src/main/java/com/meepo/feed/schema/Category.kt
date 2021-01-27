package com.meepo.feed.schema

import java.io.Serializable

data class Category(
    val id: String,
    val name: String
) : Serializable