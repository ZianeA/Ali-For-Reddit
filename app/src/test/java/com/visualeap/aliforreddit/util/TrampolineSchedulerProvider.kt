package com.visualeap.aliforreddit.util

import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TrampolineSchedulerProvider : SchedulerProvider {
    override val io: Scheduler = Schedulers.trampoline()
    override val computation: Scheduler = Schedulers.trampoline()
    override val ui: Scheduler = Schedulers.trampoline()
}