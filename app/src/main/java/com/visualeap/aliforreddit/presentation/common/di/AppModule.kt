package com.visualeap.aliforreddit.presentation.common.di

import android.app.Application
import com.visualeap.aliforreddit.R
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Named

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