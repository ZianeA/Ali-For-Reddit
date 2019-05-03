package com.visualeap.aliforreddit.core

import android.annotation.SuppressLint
import com.visualeap.aliforreddit.R
import android.app.Application
import android.util.Log
import com.visualeap.aliforreddit.data.entity.Token
import com.visualeap.aliforreddit.data.RedditService
import com.visualeap.aliforreddit.data.TokenAuthenticator
import com.visualeap.aliforreddit.presentation.util.AsyncSchedulerProvider
import com.visualeap.aliforreddit.presentation.util.applySchedulers
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import java.net.HttpURLConnection

class AliForRedditApp : Application() {

    lateinit var reddit: RedditService
    private val tag = AliForRedditApp::class.java.simpleName

    @SuppressLint("CheckResult")
    override fun onCreate() {
        super.onCreate()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .authenticator(TokenAuthenticator(
                Token(
                    "",
                    "",
                    "",
                    ""
                )
            ))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.reddit.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

        reddit = retrofit.create<RedditService>(RedditService::class.java)

//        reddit.getAccessToken(
//            """https://oauth.reddit.com/grants/installed_client""",
//            UUID.randomUUID().toString(),
//            Credentials.basic(getString(R.string.client_id), "")
//        ).applySchedulers(AsyncSchedulerProvider())
//            .subscribe({ Log.i(tag, it.accessToken) }, {
//                if(it is HttpException){
//                    if(it.response().code() == HttpURLConnection.HTTP_UNAUTHORIZED){
//                        Log.e(tag, "expired token")
//                    }
//                }
//            })
    }
}