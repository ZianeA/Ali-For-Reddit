package com.visualeap.aliforreddit.data.network

import android.app.Application
import android.util.Log
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.entity.Token
import com.visualeap.aliforreddit.domain.usecase.AuthService
import com.visualeap.aliforreddit.domain.usecase.RefreshAccessToken
import com.visualeap.aliforreddit.domain.util.HttpHeaders
import okhttp3.*
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val authService: AuthService,
    private val refreshAccessToken: RefreshAccessToken
) : Authenticator {

    private val tag = TokenAuthenticator::class.java.simpleName

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request().header(ATTEMPT_HEADER) != null) {
            return null; // Give up, we've already attempted to authenticate.
        }

        synchronized(this) {
            val token = refreshAccessToken.execute(Unit) ?: return null

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