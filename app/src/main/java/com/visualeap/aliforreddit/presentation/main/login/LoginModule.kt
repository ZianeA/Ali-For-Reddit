package com.visualeap.aliforreddit.presentation.main.login

import com.visualeap.aliforreddit.presentation.di.FragmentScope
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
class LoginModule {
    @Provides
    fun provideLoginView(loginFragment: LoginFragment): LoginView = loginFragment
}