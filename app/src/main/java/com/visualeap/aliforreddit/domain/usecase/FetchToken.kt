package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.data.network.auth.AuthService
import com.visualeap.aliforreddit.domain.util.BasicAuthCredentialProvider
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import io.reactivex.Single
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchToken @Inject constructor(
    private val authService: AuthService,
    private val tokenRepository: TokenRepository,
    private val authCredentialProvider: BasicAuthCredentialProvider
) {
    companion object {
        private const val GRANT_TYPE = "https://oauth.reddit.com/grants/installed_client"
    }

    fun execute(): Single<Token> {
        val deviceId = generateUniqueId()
        return tokenRepository.getCurrentToken()
            .switchIfEmpty(Single.defer {
                //This is the first app launch, should fetch user-less token.
                authService.getUserlessToken(
                    GRANT_TYPE,
                    deviceId,
                    authCredentialProvider.getAuthCredential()
                )
                    .map { UserlessToken(0, it.accessToken, it.type, deviceId) }
                    .flatMap { token ->
                        tokenRepository.setUserlessToken(token)
                            .flatMap { id ->
                                val tokenWithId = token.copy(id = id)
                                tokenRepository.setCurrentToken(tokenWithId)
                                    .andThen(Single.just(tokenWithId))
                            }
                    }
            })
    }

    private fun generateUniqueId() = UUID.randomUUID().toString()
}