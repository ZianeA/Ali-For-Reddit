package com.visualeap.aliforreddit.domain.authentication

import com.visualeap.aliforreddit.data.token.AuthService
import com.visualeap.aliforreddit.domain.util.BasicAuthCredentialProvider
import com.visualeap.aliforreddit.domain.util.OAuthException
import com.visualeap.aliforreddit.domain.util.TokenResponseMapper
import dagger.Reusable
import io.reactivex.*
import okhttp3.HttpUrl
import java.net.MalformedURLException
import javax.inject.Inject

@Reusable
class AuthenticateUser @Inject constructor(
    private val authService: AuthService,
    private val tokenRepository: TokenRepository,
    private val authCredentialProvider: BasicAuthCredentialProvider,
    private val tokenMapper: TokenResponseMapper
) {
    companion object {
        private const val GRANT_TYPE = "authorization_code"
        private const val PARAM_STATE = "state"
    }

    fun execute(
        redirectUrl: String,
        finalUrl: String,
        authUrl: String
    ): Completable {
        return Single.defer {
            val parsedFinalUrl = HttpUrl.parse(finalUrl) ?: throw MalformedURLException()

            parsedFinalUrl.queryParameter("error")
                ?.let { throw OAuthException(
                    "Reddit responded with error: $it"
                )
                }

            val parsedAuthUrl = HttpUrl.parse(authUrl) ?: throw MalformedURLException()
            val requestState = parsedAuthUrl.queryParameter(PARAM_STATE)!!
            val responseState = parsedFinalUrl.queryParameter(PARAM_STATE)!!
            if (requestState != responseState) throw IllegalStateException("State doesn't match")

            val code = parsedFinalUrl.queryParameter("code")!!
            // Fetch user token and set it as the current token.
            authService.getUserToken(
                GRANT_TYPE,
                code,
                redirectUrl,
                authCredentialProvider.getAuthCredential()
            )
        }
            .map { tokenMapper.toUserToken(it, 0) }
            .flatMapCompletable { userToken ->
                tokenRepository.addUserToken(userToken)
                    .flatMapCompletable { rowId -> tokenRepository.setCurrentToken(userToken.copy(id = rowId)) }
            }
    }
}