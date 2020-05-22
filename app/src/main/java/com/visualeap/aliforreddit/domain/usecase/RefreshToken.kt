package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.data.network.auth.AuthService
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import com.visualeap.aliforreddit.domain.usecase.base.SingleUseCase
import dagger.Reusable
import io.reactivex.Single
import okhttp3.Credentials
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.IllegalStateException

@Singleton
class RefreshToken @Inject constructor(
    private val authService: AuthService,
    private val tokenRepository: TokenRepository
) {
    companion object {
        private const val REFRESH_TOKEN_GRANT_TYPE = "refresh_token"
        private const val USERLESS_TOKEN_GRANT_TYPE =
            "https://oauth.reddit.com/grants/installed_client"
    }

    fun execute(clientId: String): Single<Token> {
        return tokenRepository.getCurrentToken()
            .toSingle()
            .flatMap { cachedToken ->
                when (cachedToken) {
                    is UserToken -> {
                        authService.refreshUserToken(
                            REFRESH_TOKEN_GRANT_TYPE,
                            cachedToken.refreshToken,
                            Credentials.basic(clientId, "")
                        ).map { fetchedToken ->
                            UserToken(
                                cachedToken.id,
                                fetchedToken.accessToken,
                                fetchedToken.type,
                                fetchedToken.refreshToken!!
                            )
                        }
                            .flatMap { updatedToken ->
                                tokenRepository.updateUserToken(updatedToken)
                                    .andThen(Single.just(updatedToken))
                            }
                    }
                    is UserlessToken -> {
                        authService.getUserlessToken(
                            USERLESS_TOKEN_GRANT_TYPE,
                            cachedToken.deviceId,
                            Credentials.basic(clientId, "")
                        )
                            .map { fetchedToken ->
                                UserlessToken(
                                    cachedToken.id,
                                    fetchedToken.accessToken,
                                    fetchedToken.type,
                                    cachedToken.deviceId
                                )
                            }.flatMap { updatedToken ->
                                tokenRepository.setUserlessToken(updatedToken)
                                    .map { updatedToken }
                            }
                    }
                    else -> throw IllegalStateException("Unknown Token type")
                }
            }
    }
}
