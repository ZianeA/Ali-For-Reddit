package com.visualeap.aliforreddit.data.network

import com.visualeap.aliforreddit.domain.usecase.GetToken
import com.visualeap.aliforreddit.domain.util.HttpHeaders
import dagger.Reusable
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(private val getAccessToken: GetToken) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        getAccessToken.execute(Unit)?.run {
            val newRequest = chain.request().newBuilder()
                .header(HttpHeaders.AUTHORIZATION, "$type $accessToken")
                .build()
            return chain.proceed(newRequest)

        } ?: return chain.proceed(chain.request())
    }
}