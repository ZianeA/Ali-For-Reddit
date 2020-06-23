package com.visualeap.aliforreddit.presentation.login

import dagger.Module
import dagger.Provides

@Module
class LoginModule {
    @Provides
    fun provideLoginView(loginFragment: LoginFragment): LoginView = loginFragment
}