package com.visualeap.aliforreddit.core

import android.annotation.SuppressLint
import com.visualeap.aliforreddit.R

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import net.dean.jraw.android.*
import net.dean.jraw.http.SimpleHttpLogger
import net.dean.jraw.http.UserAgent
import net.dean.jraw.oauth.AccountHelper
import java.util.*

class AliForRedditApp : Application() {

    companion object {
        lateinit var tokenStore: SharedPreferencesTokenStore
        lateinit var accountHelper: AccountHelper
        private const val PLATFORM = "android"
    }

    //TODO replace AsyncTask and remove annotation
    @SuppressLint("StaticFieldLeak")
    override fun onCreate() {
        super.onCreate()

        // Get UserAgent and OAuth2 data from api_key.xml
        //TODO - Refactor this into a class in a separate file rather than an anonymous class
        val version =
            applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0)
                .versionName
        val provider = object : AppInfoProvider {
            override fun provide() = AppInfo(
                getString(R.string.client_id), getString(R.string.redirect_url),
                UserAgent(
                    PLATFORM,
                    applicationContext.packageName,
                    version,
                    getString(R.string.reddit_username)
                )
            )
        }

        // Ideally, this should be unique to every device
        val deviceUuid = UUID.randomUUID()

        // Store our access tokens and refresh tokens in shared preferences
        tokenStore = SharedPreferencesTokenStore(applicationContext)
        // Load stored tokens into memory
        tokenStore.load()
        // Automatically save new tokens as they arrive
        tokenStore.autoPersist = true

        // An AccountHelper manages switching between accounts and into/out of userless mode.
        accountHelper = AndroidHelper.accountHelper(provider, deviceUuid, tokenStore)

        // Every time we use the AccountHelper to switch between accounts (from one account to another,
        // or into/out of userless mode), call this function
        accountHelper.onSwitch {

            // By default, JRAW logs HTTP activity to System.out. We're going to use Log.i() instead.
            val logAdapter = SimpleAndroidLogAdapter(Log.INFO)

            // We're going to use the LogAdapter to write down the summaries produced by SimpleHttpLogger
            it.logger = SimpleHttpLogger(SimpleHttpLogger.DEFAULT_LINE_LENGTH, logAdapter)

            // If you want to disable logging, use a NoopHttpLogger instead:
            // it.logger = NoopHttpLogger()
        }

        object : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg p0: Void?): Void? {
                accountHelper.switchToUserless()
                return null
            }
        }.execute()
    }
}