package com.visualeap.aliforreddit.presentation.login

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.di.FragmentScope
import com.visualeap.aliforreddit.domain.entity.Credentials
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.android.ContributesAndroidInjector
import javax.inject.Named

@Module
class LoginModule {

    @Reusable
    @Provides
    fun provideCredentials(
        @Named("clientId") clientId: String,
        @Named("redirectUrl") redirectUrl: String
    ) = Credentials(clientId, redirectUrl)

    @Provides
    fun provideLoginView(loginFragment: LoginFragment): LoginView = loginFragment

    @Module
    interface ActivityModule {
        @FragmentScope
        @ContributesAndroidInjector(modules = [LoginModule::class])
        fun loginFragment(): LoginFragment
    }
}