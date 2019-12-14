package com.visualeap.aliforreddit.data.repository.redditor

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class RedditorResponse(
    val id: String,
    @Json(name = "name") val username: String,
    @Json(name = "created_utc") val creationDate: Long,
    @Json(name = "link_karma") val linkKarma: Int,
    @Json(name = "comment_karma") val commentKarma: Int,
    @Json(name = "icon_img") val iconUrl: String,
    val coins: Int
)