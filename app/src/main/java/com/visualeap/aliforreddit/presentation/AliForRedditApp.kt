package com.visualeap.aliforreddit.presentation

import android.app.Activity
import android.app.Application
import com.visualeap.aliforreddit.presentation.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import java.net.HttpURLConnection
import javax.inject.Inject

class AliForRedditApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    override fun onCreate() {
        DaggerAppComponent.factory()
            .create(this)
            .inject(this)

        super.onCreate()

        //TODO remove this
//        reddit.getAccessToken(
//            """https://oauth.reddit.com/grants/installed_client""",
//            UUID.randomUUID().toString(),
//            Credentials.basic(getString(R.string.client_id), "")
//        ).applySchedulers(AsyncSchedulerProvider())
//            .subscribe({ Log.i(tag, it.accessToken) }, {
//                if(it is HttpException){
//                    if(it.response().finalUrl() == HttpURLConnection.HTTP_UNAUTHORIZED){
//                        Log.e(tag, "expired token")
//                    }
//                }
//            })
    }
}