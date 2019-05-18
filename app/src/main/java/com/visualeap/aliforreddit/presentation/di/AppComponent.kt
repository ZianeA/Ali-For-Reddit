package com.visualeap.aliforreddit.presentation.di

import com.visualeap.aliforreddit.presentation.AliForRedditApp
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProviderModule
import com.visualeap.aliforreddit.data.repository.RepositoryModule
import com.visualeap.aliforreddit.domain.usecase.AuthModule
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, ActivityBindingModule::class,
        SchedulerProviderModule::class, RepositoryModule::class, AuthModule::class]
)
interface AppComponent {
    fun inject(application: AliForRedditApp)
}