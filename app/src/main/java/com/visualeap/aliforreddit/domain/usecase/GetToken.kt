package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.entity.Account
import com.visualeap.aliforreddit.domain.entity.token.Token
import com.visualeap.aliforreddit.domain.entity.token.UserlessToken
import com.visualeap.aliforreddit.domain.repository.AccountRepository
import com.visualeap.aliforreddit.domain.usecase.base.NonReactiveUseCase
import dagger.Reusable
import java.util.*
import javax.inject.Inject

@Reusable
class GetToken @Inject constructor(
    private val getCurrentAccount: GetCurrentAccount,
    private val accountRepository: AccountRepository,
    private val getUserLessToken: GetUserLessToken
) :
    NonReactiveUseCase<Token?, Unit> {

    override fun execute(params: Unit): Token? {
        var token: Token? = null

        val currentAccount = getCurrentAccount.execute(Unit)

        if (currentAccount != null) {
            token = currentAccount.token
        } else {
            //This is the first app launch
            getUserLessToken.execute(generateUniqueId()).let {
                token = it
                if (it != null) saveUserlessToken(it)
            }
        }

        return token
    }

    private fun generateUniqueId() = UUID.randomUUID().toString()

    private fun saveUserlessToken(token: UserlessToken) {
        //It's not possible to test or try/catch exceptions thrown inside OnError
        var throwable: Throwable? = null

        accountRepository.saveAccount(Account.createAnonymousAccount(token, true))
            .subscribe({/*Do nothing on complete*/}, {throwable = it})
            .dispose()

        throwable?.let { throw it }
    }
}