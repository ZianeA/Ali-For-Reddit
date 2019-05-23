package com.visualeap.aliforreddit

import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class SyncSchedulerProvider : SchedulerProvider {
    override val worker: Scheduler = Schedulers.trampoline()
    override val main: Scheduler = Schedulers.trampoline()
}