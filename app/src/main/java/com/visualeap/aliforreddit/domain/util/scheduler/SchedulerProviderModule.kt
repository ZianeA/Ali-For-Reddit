package com.visualeap.aliforreddit.domain.util.scheduler

import dagger.Binds
import dagger.Module

@Module
abstract class SchedulerProviderModule {

    @Binds
    abstract fun provideSchedulerProvider(schedulerProvider: AsyncSchedulerProvider): SchedulerProvider
}