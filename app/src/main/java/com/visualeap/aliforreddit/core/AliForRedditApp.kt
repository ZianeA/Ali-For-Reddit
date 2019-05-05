package com.visualeap.aliforreddit.core

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import com.visualeap.aliforreddit.core.di.DaggerAppComponent
import com.visualeap.aliforreddit.domain.entity.Token
import com.visualeap.aliforreddit.data.network.RedditService
import com.visualeap.aliforreddit.data.network.TokenAuthenticator
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

class AliForRedditApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    lateinit var reddit: RedditService
    private val tag = AliForRedditApp::class.java.simpleName

    @SuppressLint("CheckResult")
    override fun onCreate() {
        DaggerAppComponent.builder()
            .build()
            .inject(this)

        super.onCreate()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .authenticator(
                TokenAuthenticator(
                    Token(
                        "",
                        "",
                        "",
                        ""
                    )
                )
            )
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