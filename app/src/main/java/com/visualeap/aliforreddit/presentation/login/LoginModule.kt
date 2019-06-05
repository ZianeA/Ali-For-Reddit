package com.visualeap.aliforreddit.presentation.login

import com.visualeap.aliforreddit.presentation.di.FragmentScope
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.android.ContributesAndroidInjector
import javax.inject.Named

@Module
class LoginModule {

    @Provides
    fun provideLoginView(loginFragment: LoginFragment): LoginView = loginFragment

    @Module
    interface ActivityModule {
        @FragmentScope
        @ContributesAndroidInjector(modules = [LoginModule::class])
        fun loginFragment(): LoginFragment
    }
}