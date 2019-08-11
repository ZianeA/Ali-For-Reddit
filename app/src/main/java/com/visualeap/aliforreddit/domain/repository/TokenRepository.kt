package com.visualeap.aliforreddit.domain.repository

import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

//TODO Move all the constants, i.e. grantType, basicAuth, to data layer repository
interface TokenRepository {
    fun getUserToken(code: String): Single<UserToken>

    fun refreshUserToken(tokenId: Int, refreshToken: String): Single<UserToken>

    fun getUserlessToken(deviceId: String): Single<UserlessToken>

    fun refreshUserlessToken(deviceId: String): Single<UserlessToken>

    fun getCurrentToken(): Maybe<Token>

    fun setCurrentToken(token: Token): Completable
}