package com.visualeap.aliforreddit.data.network.auth

import com.squareup.moshi.Moshi
import com.visualeap.aliforreddit.data.network.ApplicationJsonAdapterFactory
import com.visualeap.aliforreddit.data.token.AuthService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class AuthModule {
    @Reusable
    @Provides
    fun provideInterceptor() =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Singleton
    @Provides
    @Named("authOkHttpClient")
    fun provideAuthOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    @Singleton
    @Provides
    @Named("authRetrofit")
    fun provideAuthRetrofit(@Named("authOkHttpClient") okHttpClient: OkHttpClient): Retrofit {
        //TODO Right now, there are two moshi instances being created. Refactor to have only a single moshi instance.
        val moshi = Moshi.Builder()
            .add(ApplicationJsonAdapterFactory.INSTANCE)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthService(@Named("authRetrofit") authRetrofit: Retrofit): AuthService =
        authRetrofit.create<AuthService>(
            AuthService::class.java)

    companion object {
        private const val BASE_URL = "https://www.reddit.com/"
    }
}