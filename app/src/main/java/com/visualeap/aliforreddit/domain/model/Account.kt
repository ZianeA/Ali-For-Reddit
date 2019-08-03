package com.visualeap.aliforreddit.domain.model

import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserlessToken

data class Account(
    val username: String,
    val token: Token,
    val avatarUrl: String
)