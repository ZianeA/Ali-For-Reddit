package com.visualeap.aliforreddit.presentation.util

import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import dagger.Reusable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@Reusable
class AsyncSchedulerProvider @Inject constructor() : SchedulerProvider {
    override val io: Scheduler = Schedulers.io()
    override val computation: Scheduler = Schedulers.computation()
    override val ui: Scheduler = AndroidSchedulers.mainThread()
}