package com.visualeap.aliforreddit.presentation.common.di

import android.app.Application
import com.visualeap.aliforreddit.data.database.DatabaseModule
import com.visualeap.aliforreddit.data.network.NetworkModule
import com.visualeap.aliforreddit.presentation.common.AliForRedditApp
import com.visualeap.aliforreddit.presentation.common.scheduler.SchedulerProviderModule
import com.visualeap.aliforreddit.data.common.RepositoryModule
import com.visualeap.aliforreddit.data.network.auth.AuthModule
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