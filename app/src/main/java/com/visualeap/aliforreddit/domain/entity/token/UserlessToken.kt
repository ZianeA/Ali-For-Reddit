package com.visualeap.aliforreddit.domain.entity.token

import com.squareup.moshi.Json

data class UserlessToken(
    @Json(name = "access_token") override val accessToken: String,
    @Json(name = "token_type") override val type: String,
    val deviceId: String? = null
) : Token
