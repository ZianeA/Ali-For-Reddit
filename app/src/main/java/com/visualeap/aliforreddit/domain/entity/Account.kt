package com.visualeap.aliforreddit.domain.entity

import com.visualeap.aliforreddit.domain.entity.token.Token
import com.visualeap.aliforreddit.domain.entity.token.UserlessToken

data class Account(
    val username: String,
    val token: Token,
    val isLoggedIn: Boolean,
    val avatarUrl: String? = null
) {

    companion object {
        const val ANONYMOUS_ACCOUNT_USERNAME = "Anonymous"
        const val UNKNOWN_ACCOUNT_USERNAME = "Unknown"

        fun createAnonymousAccount(userlessToken: UserlessToken, isLoggedIn: Boolean) =
            Account(ANONYMOUS_ACCOUNT_USERNAME, userlessToken, isLoggedIn)
    }
}