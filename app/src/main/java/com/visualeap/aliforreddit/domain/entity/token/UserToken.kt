package com.visualeap.aliforreddit.domain.entity.token

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserToken(
    @Json(name = "access_token") override val accessToken: String,
    @Json(name = "token_type") override val type: String,
    @Json(name = "refresh_token") val refreshToken: String? = null
) : Token