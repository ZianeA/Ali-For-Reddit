package com.visualeap.aliforreddit.domain.repository

import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface TokenRepository {
    fun setUserlessToken(token: UserlessToken): Single<Int>
    fun addUserToken(token: UserToken): Single<Int>
    fun updateUserToken(token: UserToken): Completable
    fun setCurrentToken(token: Token): Completable
    fun getCurrentToken(): Maybe<Token>
}