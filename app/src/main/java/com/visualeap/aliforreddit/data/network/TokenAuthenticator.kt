package com.visualeap.aliforreddit.data.network

import com.visualeap.aliforreddit.domain.entity.Token
import okhttp3.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor() : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request().header(AUTHORIZATION) != null) {
            return null; // Give up, we've already attempted to authenticate.
        }

        //TODO fetch token
        val token = Token("", "", "", "")

        return response.request().newBuilder()
            .header(AUTHORIZATION, "${token.type} ${token.accessToken}")
            .build();
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
    }
}