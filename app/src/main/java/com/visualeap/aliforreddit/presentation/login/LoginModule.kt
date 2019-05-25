package com.visualeap.aliforreddit.presentation.login

import com.visualeap.aliforreddit.presentation.di.FragmentScope
import com.visualeap.aliforreddit.domain.entity.AuthCredentials
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.android.ContributesAndroidInjector
import javax.inject.Named

@Module
class LoginModule {

    @Reusable
    @Provides
    fun provideAuthCredentials(
        @Named("clientId") clientId: String,
        @Named("redirectUrl") redirectUrl: String
    ) = AuthCredentials(clientId, redirectUrl)

    @Provides
    fun provideLoginView(loginFragment: LoginFragment): LoginView = loginFragment

    @Module
    interface ActivityModule {
        @FragmentScope
        @ContributesAndroidInjector(modules = [LoginModule::class])
        fun loginFragment(): LoginFragment
    }
}