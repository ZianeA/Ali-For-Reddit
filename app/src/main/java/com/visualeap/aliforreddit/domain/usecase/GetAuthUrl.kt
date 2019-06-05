package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.usecase.base.NonReactiveUseCase
import dagger.Reusable
import okhttp3.HttpUrl
import javax.inject.Inject
import javax.inject.Named

@Reusable
class GetAuthUrl @Inject constructor(
    @Named("clientId") private val clientId: String,
    @Named("redirectUrl") private val redirectUrl: String
) :
    NonReactiveUseCase<String, String> {

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

    /**
     * @param params the state. A unique, possibly random, string for each authorization request.
     */
    override fun execute(params: String) = HttpUrl.parse(BASE_URL)!!
        .newBuilder()
        .addQueryParameter(CLIENT_ID, clientId)
        .addQueryParameter(RESPONSE_TYPE, CODE)
        .addQueryParameter(STATE, params)
        .addQueryParameter(REDIRECT_URL, redirectUrl)
        .addQueryParameter(DURATION, PERMANENT)
        .addQueryParameter(SCOPE, "identity edit read mysubreddits")
        .build()
        .toString()
}