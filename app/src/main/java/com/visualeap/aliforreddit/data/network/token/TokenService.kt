package com.visualeap.aliforreddit.data.network.token

import com.visualeap.aliforreddit.data.repository.token.TokenRemoteSource
import dagger.Reusable
import io.reactivex.Single
import javax.inject.Inject

@Reusable
class TokenService @Inject constructor(): TokenRemoteSource {
    override fun refreshUserToken(refreshToken: String): Single<TokenResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserlessToken(deviceId: String): Single<TokenResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserToken(code: String): Single<TokenResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}