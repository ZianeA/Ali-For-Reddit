package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.core.util.scheduler.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Observable

class AuthenticateUser(private val schedulerProvider: SchedulerProvider) :
    UseCase<Completable, Unit>(schedulerProvider) {

    override fun createObservable(params: Unit): Observable<Completable> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}