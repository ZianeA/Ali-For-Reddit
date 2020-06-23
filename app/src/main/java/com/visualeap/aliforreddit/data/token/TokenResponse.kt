package com.visualeap.aliforreddit.data.token

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

/*@JsonClass(generateAdapter = true)*/@JsonSerializable
data class TokenResponse(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type") val type: String,
    @Json(name = "refresh_token") val refreshToken: String? = null
)