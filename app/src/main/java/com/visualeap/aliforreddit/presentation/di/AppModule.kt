package com.visualeap.aliforreddit.presentation.di

import android.app.Application
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.AliForRedditApp
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Reusable
    @Provides
    @Named("clientId")
    fun provideClientId(app: Application): String = app.getString(R.string.client_id)

    @Reusable
    @Provides
    @Named("redirectUrl")
    fun provideRedirectUrl(app: Application): String = app.getString(R.string.redirect_url)
}