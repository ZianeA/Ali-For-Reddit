package com.visualeap.aliforreddit.presentation.login

import okhttp3.HttpUrl
import java.math.BigInteger
import java.net.URI
import java.security.SecureRandom

class LoginPresenter(
    private val view: LoginView,
    private val clientId: String,
    private val redirectUrl: String
) {

    fun start() {
        //TODO refactor this to a new class AuthorizationUrlBuilder
        val state = BigInteger(128, SecureRandom()).toString(32)
        val authUrl = HttpUrl.parse("https://www.reddit.com/api/v1/authorize.compact")
            ?.newBuilder()
            ?.addQueryParameter("client_id", clientId)
            ?.addQueryParameter("response_type", "code")
            ?.addQueryParameter("state", state)
            ?.addQueryParameter("redirect_uri", redirectUrl)
            ?.addQueryParameter("duration", "permanent")
            ?.addQueryParameter("scope", "identity edit read mysubreddits")
            ?.build()
            .toString()

        view.showLoginPage(authUrl)
    }

    fun onPageStarted(url: String) {
        if (hasFinishedLoading(url)) {
            view.hideLoginPage()

            //Authenticate user

        }
    }

    private fun hasFinishedLoading(url: String) = url.startsWith(redirectUrl)

}