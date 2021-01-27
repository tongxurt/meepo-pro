package com.meepo.feed.schema

import com.meepo.feed.Store
import java.io.Serializable

data class SearchMetadata(
    val searchHint: String,
    val searchKeywords: List<SearchKeyword>
) : Serializable {
    data class SearchKeyword(
        val id: String,
        val name: String,
        val icon: String = ""
    )

}