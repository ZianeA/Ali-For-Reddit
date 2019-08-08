package com.visualeap.aliforreddit.data.repository.token

import com.visualeap.aliforreddit.data.network.token.TokenResponse
import io.reactivex.Single

interface TokenRemoteSource {
    fun getUserToken(code: String): Single<TokenResponse>
    fun getUserlessToken(deviceId: String): Single<TokenResponse>
    fun refreshUserToken(refreshToken: String): Single<TokenResponse>
//    fun refreshUserlessToken(deviceId: String): Single<TokenResponse>
}