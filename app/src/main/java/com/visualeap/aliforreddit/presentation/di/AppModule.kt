package com.visualeap.aliforreddit.presentation.di

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.AliForRedditApp
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    @Named("clientId")
    fun provideClientId(app: AliForRedditApp) = app.getString(R.string.client_id)

    @Singleton
    @Provides
    @Named("redirectUrl")
    fun provideRedirectUrl(app: AliForRedditApp) = app.getString(R.string.redirect_url)
}