package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import com.visualeap.aliforreddit.domain.entity.Credentials
import com.visualeap.aliforreddit.domain.entity.Token
import com.visualeap.aliforreddit.domain.usecase.AuthenticateUser.*
import com.visualeap.aliforreddit.domain.usecase.base.SingleUseCase
import dagger.Reusable
import io.reactivex.Single
import okhttp3.HttpUrl
import java.net.MalformedURLException
import javax.inject.Inject

@Reusable
class AuthenticateUser @Inject constructor(schedulerProvider: SchedulerProvider) :
    SingleUseCase<Token, Params>(schedulerProvider) {

    companion object {
        private const val AUTHORIZATION_CODE = "authorization_code"
    }

    override fun createObservable(params: Params): Single<Token> {
        params.run {
            val parsedFinalUrl = HttpUrl.parse(finalUrl)
                ?: return Single.error(MalformedURLException())

            parsedFinalUrl.queryParameter("error")
                ?.let { return Single.error(OAuthException("Reddit responded with error: $it")) }

            parsedFinalUrl.queryParameter("state")
                ?.let { stateReceived ->
                    if (state != stateReceived)
                        return Single.error(IllegalStateException("State doesn't match"))
                }
                ?: return Single.error(IllegalArgumentException("Final redirect URL did not contain the 'state' query parameter"))


            val code = parsedFinalUrl.queryParameter("code")
                ?: return Single.error(IllegalArgumentException("Final redirect URL did not contain the 'code' query parameter"))


            val authCredentials = okhttp3.Credentials.basic(credentials.clientId, "")
            return authService.getAccessToken(
                AUTHORIZATION_CODE,
                code,
                credentials.redirectUrl,
                authCredentials
            )
        }
    }

    data class Params(
        val authService: AuthService,
        val finalUrl: String,
        val credentials: Credentials,
        val state: String
    )
}