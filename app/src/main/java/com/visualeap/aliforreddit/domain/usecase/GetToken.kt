package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.model.Account
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import com.visualeap.aliforreddit.domain.repository.AccountRepository
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import com.visualeap.aliforreddit.domain.usecase.base.NonReactiveUseCase
import com.visualeap.aliforreddit.domain.usecase.base.SingleUseCase
import dagger.Reusable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

@Reusable
class GetToken @Inject constructor(private val tokenRepository: TokenRepository) :
    SingleUseCase<Token, Unit> {

    override fun execute(params: Unit): Single<Token> {
        return tokenRepository.getCurrentToken()
            .switchIfEmpty(
                //This is "probably" the first app launch. We should just get the user-less token.
                tokenRepository.getUserLessToken(generateUniqueId())
            )
    }

    private fun generateUniqueId() = UUID.randomUUID().toString()

    //TODO Remove this
/*    private fun saveUserlessToken(token: UserlessToken) {
        //It's not possible to test or try/catch exceptions thrown inside OnError
        var throwable: Throwable? = null

        accountRepository.saveAccount(Account.createAnonymousAccount(token, true))
            .subscribe({ *//*Do nothing on complete*//* }, { throwable = it })
            .dispose()

        throwable?.let { throw it }
    }*/
}