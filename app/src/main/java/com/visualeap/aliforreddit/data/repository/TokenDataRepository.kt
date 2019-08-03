package com.visualeap.aliforreddit.data.repository

import com.visualeap.aliforreddit.data.network.AuthService
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenDataRepository @Inject constructor(private val authService: AuthService) :
    TokenRepository {

    override fun getCurrentToken(): Maybe<Token> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserToken(
        grantType: String,
        code: String,
        redirectUrl: String,
        basicAuth: String
    ): Single<UserToken> {
        return authService.getUserToken(grantType, code, redirectUrl, basicAuth)
    }

    override fun getUserLessToken(
        grantType: String,
        deviceId: String,
        basicAuth: String
    ): Single<UserlessToken> {
        return authService.getUserLessToken(grantType, deviceId, basicAuth)
    }

    override fun getRefreshedUserToken(grantType: String, refreshToken: String): Single<UserToken> {
        return authService.refreshUserToken(grantType, refreshToken)
    }
}