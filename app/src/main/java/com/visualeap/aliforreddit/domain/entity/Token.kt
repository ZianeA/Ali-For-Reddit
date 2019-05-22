package com.visualeap.aliforreddit.domain.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Token(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type") val type: String,
    //TODO remove this comment if it's stays unused
//    @Json(name = "expires_in") val expirationTime: String,
    @Json(name = "refresh_token") val refreshToken: String?
)