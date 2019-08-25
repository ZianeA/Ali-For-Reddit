package com.visualeap.aliforreddit.data.repository.token

import com.visualeap.aliforreddit.data.network.token.TokenResponse
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import io.reactivex.Single

interface TokenRemoteSource {
    fun getUserToken(code: String): Single<UserToken>
    fun getUserlessToken(deviceId: String): Single<UserlessToken>
    fun refreshUserToken(refreshToken: String): Single<UserToken>
}