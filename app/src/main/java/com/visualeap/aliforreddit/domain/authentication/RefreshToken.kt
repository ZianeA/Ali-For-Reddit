package com.visualeap.aliforreddit.domain.authentication

import com.visualeap.aliforreddit.data.token.AuthService
import com.visualeap.aliforreddit.domain.util.BasicAuthCredentialProvider
import com.visualeap.aliforreddit.domain.authentication.token.Token
import com.visualeap.aliforreddit.domain.authentication.token.UserToken
import com.visualeap.aliforreddit.domain.authentication.token.UserlessToken
import com.visualeap.aliforreddit.domain.util.TokenResponseMapper
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.IllegalStateException

@Singleton
class RefreshToken @Inject constructor(
    private val authService: AuthService,
    private val tokenRepository: TokenRepository,
    private val authCredentialProvider: BasicAuthCredentialProvider,
    private val tokenMapper: TokenResponseMapper
) {
    companion object {
        private const val REFRESH_TOKEN_GRANT_TYPE = "refresh_token"
        private const val USERLESS_TOKEN_GRANT_TYPE =
            "https://oauth.reddit.com/grants/installed_client"
    }

    fun execute(): Single<Token> {
        return tokenRepository.getCurrentToken()
            .flatMap { cachedToken: Token ->
                when (cachedToken) {
                    is UserToken -> refreshUserToken(cachedToken)
                    is UserlessToken -> refreshUserlessToken(cachedToken)
                    else -> throw IllegalStateException("Unknown Token type")
                }
            }
    }

    private fun refreshUserlessToken(cachedToken: UserlessToken): Single<UserlessToken> {
        return authService.getUserlessToken(
            USERLESS_TOKEN_GRANT_TYPE,
            cachedToken.deviceId,
            authCredentialProvider.getAuthCredential()
        )
            .map { fetchedToken ->
                tokenMapper.toUserlessToken(fetchedToken, cachedToken.id, cachedToken.deviceId)
            }
            .flatMap { updatedToken ->
                tokenRepository.setUserlessToken(updatedToken)
                    .map { updatedToken }
            }
    }

    private fun refreshUserToken(cachedToken: UserToken): Single<UserToken> {
        return authService.refreshUserToken(
            REFRESH_TOKEN_GRANT_TYPE,
            cachedToken.refreshToken,
            authCredentialProvider.getAuthCredential()
        )
            .map { fetchedToken -> tokenMapper.toUserToken(fetchedToken, cachedToken.id) }
            .flatMap { updatedToken ->
                tokenRepository.updateUserToken(updatedToken)
                    .andThen(Single.just(updatedToken))
            }
    }
}
