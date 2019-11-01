package com.visualeap.aliforreddit.data.network

import com.squareup.moshi.Moshi
import com.visualeap.aliforreddit.data.network.auth.TokenAuthenticator
import com.visualeap.aliforreddit.data.network.auth.TokenInterceptor
import com.visualeap.aliforreddit.data.repository.comment.jsonparser.CommentJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    @Named("okHttpClient")
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        tokenInterceptor: TokenInterceptor,
        authenticator: TokenAuthenticator
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .authenticator(authenticator)
            .build()

    @Singleton
    @Provides
    @Named("retrofit")
    fun provideRetrofit(@Named("okHttpClient") okHttpClient: OkHttpClient): Retrofit {
        //TODO Refactor
        val moshi = Moshi.Builder()
            .add(CommentJsonAdapterFactory())
            .add(ApplicationJsonAdapterFactory.INSTANCE)
            /*.add(KotlinJsonAdapterFactory())*/ //TODO Remove
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
    fun provideRedditService(@Named("retrofit") retrofit: Retrofit): RedditService =
        retrofit.create<RedditService>(RedditService::class.java)

    companion object {
        private const val BASE_URL = "https://oauth.reddit.com/"
    }
}