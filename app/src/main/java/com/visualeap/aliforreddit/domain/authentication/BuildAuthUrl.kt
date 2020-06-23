package com.visualeap.aliforreddit.domain.authentication

import com.visualeap.aliforreddit.domain.util.UniqueStringGenerator
import dagger.Reusable
import okhttp3.HttpUrl
import javax.inject.Inject

@Reusable
class BuildAuthUrl @Inject constructor() {
    companion object {
        private const val BASE_URL = "https://www.reddit.com/api/v1/authorize.compact"
        private const val PARAM_CLIENT_ID = "client_id"
        private const val PARAM_RESPONSE_TYPE = "response_type"
        private const val PARAM_STATE = "state"
        private const val PARAM_REDIRECT_URL = "redirect_uri"
        private const val PARAM_DURATION = "duration"
        private const val PARAM_SCOPE = "scope"
        private const val CODE = "code"
        private const val PERMANENT = "permanent"
    }

    fun execute(clientId: String, redirectUrl: String) = HttpUrl.parse(BASE_URL)!!
        .newBuilder()
        .addQueryParameter(PARAM_CLIENT_ID, clientId)
        .addQueryParameter(
            PARAM_RESPONSE_TYPE,
            CODE
        )
        .addQueryParameter(PARAM_STATE, UniqueStringGenerator.generate())
        .addQueryParameter(PARAM_REDIRECT_URL, redirectUrl)
        .addQueryParameter(
            PARAM_DURATION,
            PERMANENT
        )
        .addQueryParameter(PARAM_SCOPE, "identity edit read mysubreddits")
        .build()
        .toString()
}