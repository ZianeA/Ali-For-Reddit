package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.data.network.RedditService
import com.visualeap.aliforreddit.domain.model.Account
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import com.visualeap.aliforreddit.domain.repository.AccountRepository
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import com.visualeap.aliforreddit.domain.usecase.AuthenticateUser.*
import com.visualeap.aliforreddit.domain.usecase.base.CompletableUseCase
import dagger.Reusable
import io.reactivex.*
import okhttp3.HttpUrl
import java.net.MalformedURLException
import javax.inject.Inject
import javax.inject.Named

@Reusable
class AuthenticateUser @Inject constructor(
    schedulerProvider: SchedulerProvider,
    private val tokenRepository: TokenRepository,
    private val redditService: RedditService, //TODO remove dependency on data layer
    private val accountRepository: AccountRepository,
    private val switchLoginAccount: SwitchLoginAccount,
    @Named("redirectUrl") private val redirectUrl: String,
    @Named("basicAuth") private val basicAuth: String
) :
    CompletableUseCase<Params>(schedulerProvider) {

    companion object {
        private const val AUTHORIZATION_CODE = "authorization_code"
    }

    override fun createObservable(params: Params): Completable {

        val parsedFinalUrl = HttpUrl.parse(params.finalUrl)
            ?: return Completable.error(MalformedURLException())

        parsedFinalUrl.queryParameter("error")
            ?.let { return Completable.error(OAuthException("Reddit responded with error: $it")) }

        parsedFinalUrl.queryParameter("state")
            ?.let { stateReceived ->
                if (params.state != stateReceived)
                    return Completable.error(IllegalStateException("State doesn't match"))
            }
            ?: return Completable.error(IllegalArgumentException("Final redirect URL did not contain the 'state' query parameter"))


        val code = parsedFinalUrl.queryParameter("code")
            ?: return Completable.error(IllegalArgumentException("Final redirect URL did not contain the 'code' query parameter"))

        //The token is saved in an account with a username "unknown"
        // because the same token is going to be used to retrieve the user information, username and avatar url.
        // TokenInterceptor is going to automatically fetch it from the DB and add it to the API call header.
        return tokenRepository.getUserToken(
            AUTHORIZATION_CODE,
            code,
            redirectUrl,
            basicAuth
        ).flatMapCompletable { token ->
            accountRepository.saveAccount(
                Account(
                    Account.UNKNOWN_ACCOUNT_USERNAME,
                    token,
                    false
                )
            )
                .andThen(switchLoginAccount.execute(Account.UNKNOWN_ACCOUNT_USERNAME))
                .andThen(redditService.getCurrentUser())
                .flatMapCompletable {
                    accountRepository.updateAccount(
                        Account(
                            it.name,
                            token,
                            true,
                            it.avatarUrl
                        )
                    )
                }
        }
    }

    data class Params(
        val finalUrl: String,
        val state: String
    )
}