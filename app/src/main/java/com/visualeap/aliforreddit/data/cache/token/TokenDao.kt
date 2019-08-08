package com.visualeap.aliforreddit.data.cache.token

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.visualeap.aliforreddit.data.repository.token.TokenLocalSource
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
abstract class TokenDao: TokenLocalSource {
    @Transaction
    override fun saveUserToken(userToken: UserToken): Int {
        TODO("not implemented")
    }

    @Transaction
    override fun saveUserlessToken(userlessToken: UserlessToken): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserToken(id: Int): Single<UserToken> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserlessToken(): Single<UserlessToken> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Transaction
    override fun updateUserToken(userToken: UserToken): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Transaction
    override fun updateUserlessToken(userlessToken: UserlessToken) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCurrentToken(): Maybe<Token> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setCurrentToken(token: Token): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}