package com.visualeap.aliforreddit.data.network.auth.token

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import se.ansman.kotshi.JsonSerializable

/*@JsonClass(generateAdapter = true)*/@JsonSerializable
data class TokenResponse(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type") val type: String,
    @Json(name = "refresh_token") val refreshToken: String? = null
)