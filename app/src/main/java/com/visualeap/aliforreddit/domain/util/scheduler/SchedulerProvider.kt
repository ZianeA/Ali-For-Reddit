package com.visualeap.aliforreddit.domain.util.scheduler

import io.reactivex.Scheduler

interface SchedulerProvider {
    val io: Scheduler
    val computation: Scheduler
    val ui: Scheduler
}