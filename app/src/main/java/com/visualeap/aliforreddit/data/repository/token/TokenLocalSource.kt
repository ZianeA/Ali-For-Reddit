package com.visualeap.aliforreddit.data.repository.token

import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface TokenLocalSource {
    fun saveUserToken(userToken: UserToken): Int
    fun getUserToken(id: Int): Single<UserToken>
    fun saveUserlessToken(userlessToken: UserlessToken): Int
    fun getUserlessToken(): Single<UserlessToken>
    fun updateUserToken(userToken: UserToken)
    fun updateUserlessToken(userlessToken: UserlessToken)
    fun getCurrentToken(): Maybe<Token>
    fun setCurrentToken(token: Token): Completable
}