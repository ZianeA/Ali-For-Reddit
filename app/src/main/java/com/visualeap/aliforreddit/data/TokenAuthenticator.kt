package com.visualeap.aliforreddit.data

import com.visualeap.aliforreddit.domain.entity.Token
import okhttp3.*

class TokenAuthenticator(private val accessToken: Token) :
    Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request().header(AUTHORIZATION) != null) {
            return null; // Give up, we've already attempted to authenticate.
        }

        return response.request().newBuilder()
            .header(AUTHORIZATION, "${accessToken.type} ${accessToken.accessToken}")
            .build();
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
    }
}