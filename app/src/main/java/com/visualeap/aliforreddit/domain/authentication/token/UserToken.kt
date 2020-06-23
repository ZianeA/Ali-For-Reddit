package com.visualeap.aliforreddit.domain.authentication.token

data class UserToken(
    override val id: Int,
    override val accessToken: String,
    override val type: String,
    val refreshToken: String
) : Token