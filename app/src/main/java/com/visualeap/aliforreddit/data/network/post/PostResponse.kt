package com.visualeap.aliforreddit.data.network.post

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.visualeap.aliforreddit.domain.model.Post

@JsonClass(generateAdapter = true)
data class PostResponse(val data: Data) {
    @JsonClass(generateAdapter = true)
    data class Data(@Json(name = "after") val afterKey: String, @Json(name = "children") val postHolders: List<PostHolder>) {
        @JsonClass(generateAdapter = true)
        data class PostHolder(@Json(name = "data") val post: Post) {
            @JsonClass(generateAdapter = true)
            data class Post(
                val id: String,
                @Json(name = "author") val authorName: String,
                val title: String,
                @Json(name = "selftext") val text: String,
                val score: Int,
                @Json(name = "num_comments") val commentCount: Int
            )
        }
    }
}