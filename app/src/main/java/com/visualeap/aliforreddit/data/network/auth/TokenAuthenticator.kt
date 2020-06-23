package com.visualeap.aliforreddit.data.network.auth

import com.visualeap.aliforreddit.domain.authentication.RefreshToken
import com.visualeap.aliforreddit.domain.util.HttpHeaders
import okhttp3.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(private val refreshToken: RefreshToken) :
    Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request().header(ATTEMPT_HEADER) != null) {
            return null; // Give up, we've already attempted to authenticate.
        }

        synchronized(this) {
            // if the refreshing of the token is unsuccessful, we leave the request as is.
            val token =
                kotlin.runCatching { refreshToken.execute().blockingGet() }.getOrNull()
                    ?: return null

            return response.request().newBuilder()
                .header(HttpHeaders.AUTHORIZATION, "${token.type} ${token.accessToken}")
                .header(ATTEMPT_HEADER, ATTEMPT_HEADER_VALUE)
                .build();
        }
    }

    companion object {
        const val ATTEMPT_HEADER = "Attempt"
        const val ATTEMPT_HEADER_VALUE = "1"
    }
}