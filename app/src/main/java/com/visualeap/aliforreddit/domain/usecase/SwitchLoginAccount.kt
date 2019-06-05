package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.entity.Account
import com.visualeap.aliforreddit.domain.repository.AccountRepository
import com.visualeap.aliforreddit.domain.usecase.base.CompletableUseCase
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Observable
import java.lang.IllegalStateException
import javax.inject.Inject
import kotlin.IllegalArgumentException

@Reusable
class SwitchLoginAccount @Inject constructor(
    schedulerProvider: SchedulerProvider,
    private val accountRepository: AccountRepository
) :
    CompletableUseCase<String>(schedulerProvider) {

    /**
     * @param params The account's username
     */
    override fun createObservable(params: String): Completable {
        return accountRepository.getAccounts()
            .doOnSuccess { accounts ->
                if (accounts.size < 2) {
                    throw IllegalStateException("There should be at least two accounts")
                }
                if (!accounts.any { it.username == params }) {
                    throw IllegalArgumentException("Invalid username")
                }
            }
            .flatMap { accounts ->
                Observable.fromIterable(accounts)
                    .flatMap {
                        if (it.isLoggedIn) {
                            accountRepository.updateAccount(
                                it.copy(
                                    isLoggedIn = false
                                )
                            ).toObservable()
                        } else Observable.just(it)
                    }
                    .filter { it.username == params }
                    .flatMap {
                        accountRepository.updateAccount(it.copy(isLoggedIn = true))
                            .toObservable<Account>()
                    }
                    .toList()
            }
            .ignoreElement()

    }
}