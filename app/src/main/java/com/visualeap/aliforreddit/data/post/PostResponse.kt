package com.visualeap.aliforreddit.data.post

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class PostResponse(val data: Data) {
    @JsonSerializable
    data class Data(@Json(name = "after") val afterKey: String?, @Json(name = "children") val postHolders: List<PostHolder>) {
        @JsonSerializable
        data class PostHolder(@Json(name = "data") val post: Post) {
            @JsonSerializable
            data class Post(
                @Json(name = "name") val id: String,
                @Json(name = "author") val authorName: String,
                val title: String,
                @Json(name = "selftext") val text: String,
                val score: Int,
                @Json(name = "num_comments") val commentCount: Int,
                @Json(name = "subreddit_id") val subredditId: String,
                @Json(name = "created_utc") val created: Long
            )
        }
    }
}