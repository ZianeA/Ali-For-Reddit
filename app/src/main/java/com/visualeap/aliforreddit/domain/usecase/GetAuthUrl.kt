package com.visualeap.aliforreddit.domain.usecase

import android.nfc.FormatException
import com.visualeap.aliforreddit.domain.entity.Credentials
import com.visualeap.aliforreddit.domain.usecase.GetAuthUrl.*
import com.visualeap.aliforreddit.domain.usecase.base.NonReactiveUseCase
import dagger.Reusable
import okhttp3.HttpUrl
import javax.inject.Inject

@Reusable
class GetAuthUrl @Inject constructor() : NonReactiveUseCase<String, Params> {

    companion object {
        private const val BASE_URL = "https://www.reddit.com/api/v1/authorize.compact"
        private const val CLIENT_ID = "client_id"
        private const val RESPONSE_TYPE = "response_type"
        private const val STATE = "state"
        private const val REDIRECT_URL = "redirect_uri"
        private const val DURATION = "duration"
        private const val SCOPE = "scope"
        private const val CODE = "code"
        private const val PERMANENT = "permanent"
    }

    override fun execute(params: Params) = HttpUrl.parse(BASE_URL)!!
        .newBuilder()
        .addQueryParameter(CLIENT_ID, params.credentials.clientId)
        .addQueryParameter(RESPONSE_TYPE, CODE)
        .addQueryParameter(STATE, params.state)
        .addQueryParameter(REDIRECT_URL, params.credentials.redirectUrl)
        .addQueryParameter(DURATION, PERMANENT)
        .addQueryParameter(SCOPE, "identity edit read mysubreddits")
        .build()
        .toString()

    data class Params(
        val credentials: Credentials,
        val state: String
    )
}