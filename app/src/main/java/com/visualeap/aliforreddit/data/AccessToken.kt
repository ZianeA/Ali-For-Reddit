package com.visualeap.aliforreddit.data

import com.squareup.moshi.Json

data class AccessToken(
    @Json(name = "access_token") val value: String,
    @Json(name = "token_type") val type: String,
    @Json(name = "expires_in") val expiresIn: String
)