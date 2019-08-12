package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import com.visualeap.aliforreddit.domain.usecase.base.SingleUseCase
import dagger.Reusable
import io.reactivex.Single
import javax.inject.Inject
import kotlin.IllegalStateException

@Reusable
class RefreshToken @Inject constructor(private val tokenRepository: TokenRepository) :
    SingleUseCase<Token, Unit> {

    override fun execute(params: Unit): Single<Token> {
        return tokenRepository.getCurrentToken()
            .toSingle()
            .flatMap {
                when (it) {
                    is UserToken -> tokenRepository.refreshUserToken(it.id, it.refreshToken)
                    is UserlessToken -> tokenRepository.refreshUserlessToken(it.deviceId)
                    else -> throw IllegalStateException("Unknown Token type")
                }
            }
    }
}
