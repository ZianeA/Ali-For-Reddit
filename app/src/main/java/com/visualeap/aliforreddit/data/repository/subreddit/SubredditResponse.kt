package com.visualeap.aliforreddit.data.repository.subreddit

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class SubredditResponse(@Json(name = "data") val data: Data) {
    @JsonSerializable
    data class Data(@Json(name = "children") val subredditHolders: List<SubredditHolder>) {
        @JsonSerializable
        data class SubredditHolder(@Json(name = "data") val subreddit: Subreddit) {
            @JsonSerializable
            data class Subreddit(
                @Json(name = "name") val id: String,
                @Json(name = "display_name") val name: String,
                @Json(name = "icon_img") val iconUrl: String?,
                @Json(name = "primary_color") val primaryColor: String?,
                @Json(name = "key_color") val keyColor: String?
            )
        }
    }
}