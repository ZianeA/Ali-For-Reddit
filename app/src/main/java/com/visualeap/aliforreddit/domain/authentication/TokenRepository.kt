package com.visualeap.aliforreddit.domain.authentication

import com.visualeap.aliforreddit.domain.authentication.token.Token
import com.visualeap.aliforreddit.domain.authentication.token.UserToken
import com.visualeap.aliforreddit.domain.authentication.token.UserlessToken
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface TokenRepository {
    fun setUserlessToken(token: UserlessToken): Single<Int>
    fun addUserToken(token: UserToken): Single<Int>
    fun updateUserToken(token: UserToken): Completable
    fun setCurrentToken(token: Token): Completable
    fun getCurrentToken(): Single<Token>
}