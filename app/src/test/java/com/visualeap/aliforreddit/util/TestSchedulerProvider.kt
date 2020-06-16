package com.visualeap.aliforreddit.util

import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

class TestSchedulerProvider(testScheduler: TestScheduler) : SchedulerProvider {
    override val io: Scheduler = testScheduler
    override val computation: Scheduler = testScheduler
    override val ui: Scheduler = testScheduler
}