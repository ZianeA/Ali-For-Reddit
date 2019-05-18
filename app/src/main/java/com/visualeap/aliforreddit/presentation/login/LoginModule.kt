package com.visualeap.aliforreddit.presentation.login

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.core.di.ActivityScope
import com.visualeap.aliforreddit.core.di.FragmentScope
import com.visualeap.aliforreddit.domain.entity.Credentials
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.android.ContributesAndroidInjector
import javax.inject.Qualifier

@Module
class LoginModule {

    @Reusable
    @Provides
    fun provideCredentials(loginActivity: LoginActivity) = loginActivity.run {
        Credentials(
            getString(R.string.client_id),
            getString(R.string.redirect_url)
        )
    }

    @Provides
    fun provideLoginView(loginFragment: LoginFragment): LoginView = loginFragment

    @Module
    interface ActivityModule {
        @FragmentScope
        @ContributesAndroidInjector(modules = [LoginModule::class])
        fun loginFragment(): LoginFragment
    }
}