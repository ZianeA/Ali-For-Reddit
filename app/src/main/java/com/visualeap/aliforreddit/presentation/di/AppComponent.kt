package com.visualeap.aliforreddit.presentation.di

import com.visualeap.aliforreddit.data.network.NetworkModule
import com.visualeap.aliforreddit.presentation.AliForRedditApp
import com.visualeap.aliforreddit.presentation.util.SchedulerProviderModule
import com.visualeap.aliforreddit.data.repository.RepositoryModule
import com.visualeap.aliforreddit.data.repository.AuthModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, ActivityBindingModule::class,
        SchedulerProviderModule::class, RepositoryModule::class, AuthModule::class,
        NetworkModule::class, AppModule::class]
)
interface AppComponent {
    fun inject(application: AliForRedditApp)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: AliForRedditApp): AppComponent
    }
}