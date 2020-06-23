package com.visualeap.aliforreddit.domain.authentication

import com.visualeap.aliforreddit.domain.authentication.token.UserToken
import com.visualeap.aliforreddit.domain.authentication.token.UserlessToken
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
            .toSingle(false)
    }
}