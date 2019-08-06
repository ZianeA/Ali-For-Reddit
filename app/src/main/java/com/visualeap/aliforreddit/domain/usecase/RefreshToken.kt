package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.model.Account
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import com.visualeap.aliforreddit.domain.repository.AccountRepository
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import com.visualeap.aliforreddit.domain.usecase.base.SingleUseCase
import dagger.Reusable
import io.reactivex.Single
import java.lang.IllegalArgumentException
import javax.inject.Inject
import kotlin.IllegalStateException

@Reusable
class RefreshToken @Inject constructor(private val tokenRepository: TokenRepository) :
    SingleUseCase<Token, Unit> {

    override fun execute(params: Unit): Single<Token> {
        return tokenRepository.getCurrentToken()
            .doOnComplete { throw IllegalStateException("There must exist a token") }
            .flatMapSingle { currentToken ->
                if (currentToken is UserToken) {
                    val refreshToken = currentToken.refreshToken
//                    if (refreshToken.isNullOrBlank()) throw IllegalStateException("Refresh token cannot be null or empty")

                    tokenRepository.refreshUserToken(refreshToken)
                } else {
                    val deviceId = (currentToken as UserlessToken).deviceId
//                    if (deviceId.isNullOrBlank()) throw IllegalStateException("Device ID cannot be null or empty")

                    tokenRepository.refreshUserLessToken(deviceId)
                }
            }
    }

    //TODO caching token is the responsibility of the repository
    /*private fun saveToken(
        currentAccount: Account,
        currentToken: Token,
        newToken: Token
    ) {
        //Update the current token with new access-token
        //Because the new token doesn't contain device ID
        val token = when (currentToken) {
            is UserToken -> currentToken.copy(accessToken = newToken.accessToken)
            is UserlessToken -> currentToken.copy(accessToken = newToken.accessToken)
            else -> throw IllegalArgumentException()
        }
        //It's not possible to test or try/catch exceptions thrown inside OnError
        var throwable: Throwable? = null
        accountRepository.saveAccount(currentAccount.copy(token = token))
            .subscribe({ *//*do nothing on complete*//* }, { throwable = it })
            .dispose()

        throwable?.let { throw it }
    }*/
}
