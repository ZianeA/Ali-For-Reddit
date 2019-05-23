package com.visualeap.aliforreddit.presentation.util

import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import dagger.Reusable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@Reusable
class IoSchedulerProvider @Inject constructor() : SchedulerProvider {
    override val worker: Scheduler = Schedulers.io()
    override val main: Scheduler = AndroidSchedulers.mainThread()
}