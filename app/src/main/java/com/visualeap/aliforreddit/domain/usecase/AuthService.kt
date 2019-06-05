package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.entity.token.UserToken
import com.visualeap.aliforreddit.domain.entity.token.UserlessToken
import com.visualeap.aliforreddit.domain.util.HttpHeaders
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @FormUrlEncoded
    @POST(URL_PATH)
    fun getUserToken(
        @Field(GRANT_TYPE) grantType: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUrl: String,
        @Header(HttpHeaders.AUTHORIZATION) basicAuth: String
    ): Single<UserToken>

    @FormUrlEncoded
    @POST(URL_PATH)
    fun getUserLessToken(
        @Field(GRANT_TYPE) grantType: String,
        @Field("device_id") deviceId: String,
        @Header(HttpHeaders.AUTHORIZATION) basicAuth: String
    ): Single<UserlessToken>

    @FormUrlEncoded
    @POST(URL_PATH)
    fun refreshUserToken(
        @Field(GRANT_TYPE) grantType: String,
        @Field("refresh_token") refreshToken: String
    ): Single<UserToken>

    companion object {
        private const val URL_PATH = "api/v1/access_token"
        private const val GRANT_TYPE = "grant_type"
    }
}