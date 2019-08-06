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

    fun refreshUserToken(refreshToken: String): Single<UserToken>

    fun getUserLessToken(deviceId: String): Single<UserlessToken>

    fun refreshUserLessToken(deviceId: String): Single<UserlessToken>

    fun getCurrentToken(): Maybe<Token>
}