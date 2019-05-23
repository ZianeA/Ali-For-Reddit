package com.visualeap.aliforreddit.presentation.util

import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import dagger.Binds
import dagger.Module

@Module
abstract class SchedulerProviderModule {

    @Binds
    abstract fun provideSchedulerProvider(schedulerProvider: IoSchedulerProvider): SchedulerProvider
}