package com.visualeap.aliforreddit.domain.entity

import com.squareup.moshi.Json

data class Token(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type") val type: String,
    @Json(name = "expires_in") val expirationTime: String,
    @Json(name = "refresh_token") val refreshToken : String
)