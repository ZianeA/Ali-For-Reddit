package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.data.network.auth.AuthService
import com.visualeap.aliforreddit.domain.util.BasicAuthCredentialProvider
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import dagger.Reusable
import io.reactivex.*
import okhttp3.HttpUrl
import java.net.MalformedURLException
import javax.inject.Inject

@Reusable
class AuthenticateUser @Inject constructor(
    private val authService: AuthService,
    private val tokenRepository: TokenRepository,
    private val authCredentialProvider: BasicAuthCredentialProvider
) {
    companion object {
        private const val GRANT_TYPE = "authorization_code"
    }

    fun execute(
        redirectUrl: String,
        finalUrl: String,
        state: String
    ): Completable {
        val parsedFinalUrl = HttpUrl.parse(finalUrl)
            ?: return Completable.error(MalformedURLException())

        parsedFinalUrl.queryParameter("error")
            ?.let { return Completable.error(OAuthException("Reddit responded with error: $it")) }

        parsedFinalUrl.queryParameter("state")
            ?.let { stateReceived ->
                if (state != stateReceived)
                    return Completable.error(IllegalStateException("State doesn't match"))
            }
            ?: return Completable.error(IllegalArgumentException("Final redirect URL did not contain the 'state' query parameter"))


        val code = parsedFinalUrl.queryParameter("code")
            ?: return Completable.error(IllegalArgumentException("Final redirect URL did not contain the 'code' query parameter"))

        // Fetch user token and set it as the current token.
        return authService.getUserToken(
            GRANT_TYPE,
            code,
            redirectUrl,
            authCredentialProvider.getAuthCredential()
        )
            .map { UserToken(0, it.accessToken, it.type, it.refreshToken!!) }
            .flatMapCompletable { userToken ->
                tokenRepository.addUserToken(userToken)
                    .flatMapCompletable { rowId -> tokenRepository.setCurrentToken(userToken.copy(id = rowId)) }
            }
    }
}