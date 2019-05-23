package com.visualeap.aliforreddit.domain.util.scheduler

import io.reactivex.Scheduler

interface SchedulerProvider {
    val worker: Scheduler
    val main: Scheduler
}