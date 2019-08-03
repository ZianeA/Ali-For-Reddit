package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.model.Account
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import com.visualeap.aliforreddit.domain.repository.AccountRepository
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import com.visualeap.aliforreddit.domain.usecase.base.NonReactiveUseCase
import dagger.Reusable
import java.util.*
import javax.inject.Inject

@Reusable
class GetToken @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val getUserLessToken: GetUserLessToken
) :
    NonReactiveUseCase<Token?, Unit> {

    override fun execute(params: Unit): Token? {
        var token: Token? = null
        var throwable: Throwable? = null

        //In this case, it's not possible to test or try/catch exceptions thrown inside onSubscribe or OnError
        tokenRepository.getCurrentToken()
            .subscribe({ token = it }, { throwable = it })
            .dispose() //Dispose immediately because we can treat this as a synchronous call

        throwable?.let { throw it }

        if (token == null) {
            //This is the "probably" first app launch
            getUserLessToken.execute(generateUniqueId()).let {
                token = it
                /*if (it != null) saveUserlessToken(it)*/ //TODO Remove this. the data layer, specifically TokenRepository, should be responsible for caching tokens.
            }
        }

        return token
    }

    private fun generateUniqueId() = UUID.randomUUID().toString()

/*    private fun saveUserlessToken(token: UserlessToken) {
        //It's not possible to test or try/catch exceptions thrown inside OnError
        var throwable: Throwable? = null

        accountRepository.saveAccount(Account.createAnonymousAccount(token, true))
            .subscribe({ *//*Do nothing on complete*//* }, { throwable = it })
            .dispose()

        throwable?.let { throw it }
    }*/
}