package com.visualeap.aliforreddit.domain.authentication

import androidx.room.EmptyResultSetException
import com.visualeap.aliforreddit.domain.authentication.token.UserToken
import com.visualeap.aliforreddit.domain.authentication.token.UserlessToken
import com.visualeap.aliforreddit.domain.redditor.Redditor
import dagger.Reusable
import io.reactivex.Single
import javax.inject.Inject

@Reusable
class IsUserLoggedIn @Inject constructor(private val tokenRepository: TokenRepository) {
    fun execute(): Single<Boolean> {
        return tokenRepository.getCurrentToken()
            .map {
                when (it) {
                    is UserToken -> true
                    is UserlessToken -> false
                    else -> throw IllegalStateException("Unknown Token type")
                }
            }
            .onErrorResumeNext {
                when (it) {
                    is EmptyResultSetException -> Single.just(false)
                    else -> Single.error(it)
                }
            }
    }
}