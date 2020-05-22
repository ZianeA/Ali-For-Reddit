package com.visualeap.aliforreddit.data.network.auth

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.usecase.FetchToken
import com.visualeap.aliforreddit.domain.util.HttpHeaders
import com.visualeap.aliforreddit.presentation.common.ResourceProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(
    private val fetchToken: FetchToken,
    private val resourceProvider: ResourceProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val clientId = resourceProvider.getString(R.string.client_id)
        // if the fetching of the token is unsuccessful, we leave the request as is.
        val token = kotlin.runCatching { fetchToken.execute(clientId).blockingGet() }.getOrNull()
            ?: return chain.proceed(chain.request())

        val newRequest = chain.request()
            .newBuilder()
            .header(HttpHeaders.AUTHORIZATION, "${token.type} ${token.accessToken}")
            .build()

        return chain.proceed(newRequest)
    }
}