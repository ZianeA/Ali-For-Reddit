package com.visualeap.aliforreddit.data.network.auth

import com.visualeap.aliforreddit.domain.authentication.FetchToken
import com.visualeap.aliforreddit.domain.util.HttpHeaders
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(private val fetchToken: FetchToken) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // if the fetching of the token is unsuccessful, we leave the request as is.
        val token = kotlin.runCatching { fetchToken.execute().blockingGet() }.getOrNull()
            ?: return chain.proceed(chain.request())

        val newRequest = chain.request()
            .newBuilder()
            .header(HttpHeaders.AUTHORIZATION, "${token.type} ${token.accessToken}")
            .build()

        return chain.proceed(newRequest)
    }
}