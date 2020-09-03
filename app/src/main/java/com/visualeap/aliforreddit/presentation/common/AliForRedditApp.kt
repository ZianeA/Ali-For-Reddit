package com.visualeap.aliforreddit.presentation.common

import android.app.Activity
import androidx.multidex.MultiDexApplication
import com.visualeap.aliforreddit.BuildConfig
import com.visualeap.aliforreddit.presentation.common.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

class AliForRedditApp : MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    override fun onCreate() {
        DaggerAppComponent.factory()
            .create(this)
            .inject(this)

        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}