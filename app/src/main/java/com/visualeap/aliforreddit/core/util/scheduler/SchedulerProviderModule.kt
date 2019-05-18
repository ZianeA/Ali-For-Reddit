package com.visualeap.aliforreddit.core.util.scheduler

import dagger.Binds
import dagger.Module
import dagger.Reusable
import javax.inject.Qualifier

@Module
abstract class SchedulerProviderModule {

    @Binds
    abstract fun provideSchedulerProvider(schedulerProvider: AsyncSchedulerProvider): SchedulerProvider
}