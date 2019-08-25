package com.visualeap.aliforreddit.data.repository.token

import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface TokenLocalSource {
    fun addUserToken(userToken: UserToken): Single<Int>
    fun getUserToken(id: Int): Single<UserToken>
    fun setUserlessToken(userlessToken: UserlessToken): Completable
    fun getUserlessToken(): Single<UserlessToken>
    fun updateUserToken(userToken: UserToken): Completable
    fun getCurrentToken(): Maybe<Token>
    fun setCurrentToken(token: Token): Completable
}