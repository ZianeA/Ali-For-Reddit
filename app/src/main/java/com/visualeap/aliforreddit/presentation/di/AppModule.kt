package com.visualeap.aliforreddit.presentation.di

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.AliForRedditApp
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.Credentials
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Reusable
    @Provides
    @Named("clientId")
    fun provideClientId(app: AliForRedditApp): String = app.getString(R.string.client_id)

    @Reusable
    @Provides
    @Named("redirectUrl")
    fun provideRedirectUrl(app: AliForRedditApp): String = app.getString(R.string.redirect_url)

    @Reusable
    @Provides
    @Named("basicAuth")
    fun provideBasicAuth(@Named("clientId") clientId: String): String =
        Credentials.basic(clientId, "")
}