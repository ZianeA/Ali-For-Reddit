package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.model.Account
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import com.visualeap.aliforreddit.domain.repository.AccountRepository
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import com.visualeap.aliforreddit.domain.usecase.base.NonReactiveUseCase
import dagger.Reusable
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import javax.inject.Inject

@Reusable
class RefreshToken @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val getCurrentAccount: GetCurrentAccount,
    private val getUserLessToken: GetUserLessToken,
    private val accountRepository: AccountRepository
) :
    NonReactiveUseCase<Token?, Unit> {

    override fun execute(params: Unit): Token? {
        var newToken: Token? = null

        val currentAccount =
            getCurrentAccount.execute(Unit) ?: return null //this is probably an illegal state
        val storedToken = currentAccount.token

        if (storedToken is UserToken) {
            val refreshToken = storedToken.refreshToken
            if (refreshToken.isNullOrBlank()) throw IllegalStateException("Refresh token cannot be null or empty")

            //Dispose immediately since it's a synchronous call
            tokenRepository.getRefreshedUserToken(GRANT_TYPE, refreshToken)
                .subscribe({ newToken = it }, { /*do nothing on error*/ })
                .dispose()

            newToken?.let { saveToken(currentAccount, storedToken, it as UserToken) }

        } else {
            val deviceId = (storedToken as UserlessToken).deviceId
            if (deviceId.isNullOrBlank()) throw IllegalStateException("Device ID cannot be null or empty")

            getUserLessToken.execute(deviceId).let {
                newToken = it
                if (it != null) saveToken(currentAccount, storedToken, it)
            }
        }

        return newToken
    }

    private fun saveToken(
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
            .subscribe({ /*do nothing on complete*/ }, { throwable = it })
            .dispose()

        throwable?.let { throw it }
    }

    companion object {
        private const val GRANT_TYPE = "refresh_token"
    }
}
