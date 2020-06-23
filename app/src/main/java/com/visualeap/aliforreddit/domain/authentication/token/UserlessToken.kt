package com.visualeap.aliforreddit.domain.authentication.token

data class UserlessToken(
    override val id: Int,
    override val accessToken: String,
    override val type: String,
    val deviceId: String
) : Token
