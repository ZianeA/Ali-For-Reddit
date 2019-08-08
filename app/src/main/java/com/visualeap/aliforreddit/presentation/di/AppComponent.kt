package com.visualeap.aliforreddit.presentation.di

import android.app.Application
import com.visualeap.aliforreddit.data.cache.DatabaseModule
import com.visualeap.aliforreddit.data.network.NetworkModule
import com.visualeap.aliforreddit.presentation.AliForRedditApp
import com.visualeap.aliforreddit.presentation.util.SchedulerProviderModule
import com.visualeap.aliforreddit.data.repository.RepositoryModule
import com.visualeap.aliforreddit.data.network.AuthModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, ActivityBindingModule::class,
        SchedulerProviderModule::class, RepositoryModule::class, AuthModule::class,
        NetworkModule::class, AppModule::class, DatabaseModule::class]
)
interface AppComponent {
    fun inject(application: AliForRedditApp)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }
}