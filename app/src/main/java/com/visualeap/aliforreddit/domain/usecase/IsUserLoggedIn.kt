package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import com.visualeap.aliforreddit.domain.repository.TokenRepository
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