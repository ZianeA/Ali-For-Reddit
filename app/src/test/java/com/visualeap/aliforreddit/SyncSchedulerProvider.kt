package com.visualeap.aliforreddit

import com.visualeap.aliforreddit.core.util.scheduler.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class SyncSchedulerProvider : SchedulerProvider {
    override val io: Scheduler = Schedulers.trampoline()

    override val computation: Scheduler = Schedulers.trampoline()

    override val ui: Scheduler = Schedulers.trampoline()
}