package com.visualeap.aliforreddit.presentation.util

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

class AsyncSchedulerProvider : SchedulerProvider {
    override val io: Scheduler = Schedulers.io()

    override val computation: Scheduler = Schedulers.computation()

    override val ui: Scheduler = AndroidSchedulers.mainThread()
}