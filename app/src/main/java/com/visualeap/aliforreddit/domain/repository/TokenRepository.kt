package com.visualeap.aliforreddit.domain.repository

import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface TokenRepository {
    fun getUserToken(
        grantType: String,
        code: String,
        redirectUrl: String,
        basicAuth: String
    ): Single<UserToken>

    fun getUserLessToken(
        grantType: String,
        deviceId: String,
        basicAuth: String
    ): Single<UserlessToken>

    fun getRefreshedUserToken(grantType: String, refreshToken: String): Single<UserToken>

    fun getCurrentToken(): Maybe<Token>
}