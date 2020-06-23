package com.visualeap.aliforreddit.presentation.common.scheduler

import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import com.visualeap.aliforreddit.presentation.common.scheduler.AsyncSchedulerProvider
import dagger.Binds
import dagger.Module

@Module
abstract class SchedulerProviderModule {
    @Binds
    abstract fun provideSchedulerProvider(schedulerProvider: AsyncSchedulerProvider): SchedulerProvider
}