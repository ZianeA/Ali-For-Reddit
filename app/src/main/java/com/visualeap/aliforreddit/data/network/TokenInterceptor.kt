package com.visualeap.aliforreddit.data.network

import com.visualeap.aliforreddit.domain.usecase.GetToken
import com.visualeap.aliforreddit.domain.util.HttpHeaders
import dagger.Reusable
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

//Add unit tests
@Reusable
class TokenInterceptor @Inject constructor(private val getAccessToken: GetToken) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = getAccessToken.execute(Unit)

        val newRequest = chain.request().newBuilder()
            .header(HttpHeaders.AUTHORIZATION, "${token.type} ${token.accessToken}")
            .build()

        return chain.proceed(newRequest)
    }
}