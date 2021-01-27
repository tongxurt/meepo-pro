package com.meepo.feed.schema

import com.meepo.feed.Store
import java.io.Serializable

data class Resource(
    val content: String?,
    val resourceType: String = Store.MEDIA_TYPE_IMAGE
) : Serializable