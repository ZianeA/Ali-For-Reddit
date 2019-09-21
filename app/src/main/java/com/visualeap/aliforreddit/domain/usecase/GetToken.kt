package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import com.visualeap.aliforreddit.domain.usecase.base.SingleUseCase
import dagger.Reusable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetToken @Inject constructor(private val tokenRepository: TokenRepository) :
    SingleUseCase<Token, Unit> {

    override fun execute(params: Unit): Single<Token> {
        return tokenRepository.getCurrentToken()
            .switchIfEmpty(
                //This is "probably" the first app launch. We should just get the user-less token.
                tokenRepository.getUserlessToken(generateUniqueId())
            ).flatMap {
                tokenRepository.setCurrentToken(it)
                    .toSingle { it }
            }
    }

    private fun generateUniqueId() = UUID.randomUUID().toString()
}