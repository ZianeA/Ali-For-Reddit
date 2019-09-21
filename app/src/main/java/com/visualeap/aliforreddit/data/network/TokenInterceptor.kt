package com.visualeap.aliforreddit.data.network

import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.usecase.GetToken
import com.visualeap.aliforreddit.domain.util.HttpHeaders
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(private val getToken: GetToken) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var token: Token? = null

        getToken.execute(Unit)
            .subscribe({ token = it }, { /*on error*/ })

        val safeToken = token ?: return chain.proceed(chain.request())
        val newRequest = chain.request()
            .newBuilder()
            .header(HttpHeaders.AUTHORIZATION, "${safeToken.type} ${safeToken.accessToken}")
            .build()
        return chain.proceed(newRequest)
    }
}