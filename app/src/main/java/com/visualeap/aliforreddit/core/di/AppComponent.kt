package com.visualeap.aliforreddit.core.di

import com.visualeap.aliforreddit.core.AliForRedditApp
import com.visualeap.aliforreddit.core.util.scheduler.SchedulerProviderModule
import com.visualeap.aliforreddit.data.repository.RepositoryModule
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, ActivityBindingModule::class,
        SchedulerProviderModule::class, RepositoryModule::class]
)
interface AppComponent {
    fun inject(application: AliForRedditApp)
}