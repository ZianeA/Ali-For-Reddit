package com.visualeap.aliforreddit.domain.util

import com.visualeap.aliforreddit.data.token.TokenResponse
import com.visualeap.aliforreddit.domain.authentication.token.UserToken
import com.visualeap.aliforreddit.domain.authentication.token.UserlessToken
import dagger.Reusable
import javax.inject.Inject

@Reusable
class TokenResponseMapper @Inject constructor() {
    fun toUserToken(token: TokenResponse, id: Int): UserToken {
        return UserToken(id, token.accessToken, token.type, token.refreshToken!!)
    }

    fun toUserlessToken(token: TokenResponse, id: Int, deviceId: String): UserlessToken {
        return UserlessToken(id, token.accessToken, token.type, deviceId)
    }
}