package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.model.Account
import com.visualeap.aliforreddit.domain.repository.AccountRepository
import com.visualeap.aliforreddit.domain.usecase.base.NonReactiveUseCase
import dagger.Reusable
import java.lang.IllegalStateException
import javax.inject.Inject

//TODO delete this class. It's deprecated.
@Reusable
class GetCurrentAccount @Inject constructor(private val accountRepository: AccountRepository) :
    NonReactiveUseCase<Account?, Unit> {

    override fun execute(params: Unit): Account? {
        var currentAccount: Account? = null
        //It's not possible to test or try/catch exceptions thrown inside onSubscribe or OnError
        var accounts: List<Account>? = null
        var throwable: Throwable? = null

        accountRepository.getAccounts()
            .subscribe({ accounts = it }, { throwable = it })
            .dispose() //Dispose immediately because this is a synchronous call

        /*accounts?.apply {
            val loggedInAccounts = filter { it.isLoggedIn }
            //TODO should throw the same exception inside getCurrentToken method
            if (loggedInAccounts.size > 1) throw IllegalStateException("Multiple accounts cannot be logged-in at the same time")
            else currentAccount = loggedInAccounts.firstOrNull()
        }*/

        throwable?.let { throw it }

        return currentAccount
    }
}
