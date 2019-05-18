package com.visualeap.aliforreddit

import com.visualeap.aliforreddit.core.util.scheduler.SchedulerProvider
import dagger.Reusable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@Reusable
class SyncSchedulerProvider @Inject constructor() : SchedulerProvider {
    override val io: Scheduler = Schedulers.trampoline()

    override val computation: Scheduler = Schedulers.trampoline()

    override val ui: Scheduler = Schedulers.trampoline()
}