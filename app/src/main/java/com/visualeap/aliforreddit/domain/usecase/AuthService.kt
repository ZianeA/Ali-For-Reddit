package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.entity.Token
import com.visualeap.aliforreddit.domain.util.HttpHeaders
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @FormUrlEncoded
    @POST(URL_PATH)
    fun getAccessToken(
        @Field(GRANT_TYPE) grantType: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUrl: String,
        @Header(HttpHeaders.AUTHORIZATION) credentials: String
    ): Single<Token>

    @FormUrlEncoded
    @POST(URL_PATH)
    fun getUserLessAccessToken(
        @Field(GRANT_TYPE) grantType: String,
        @Field("device_id") deviceId: String,
        @Header(HttpHeaders.AUTHORIZATION) credentials: String
    ): Single<Token>

    @FormUrlEncoded
    @POST(URL_PATH)
    fun refreshToken(
        @Field(GRANT_TYPE) grantType: String,
        @Field("refresh_token") refreshToken: String
    ): Single<Token>

    companion object {
        private const val URL_PATH = "api/v1/access_token"
        private const val GRANT_TYPE = "grant_type"
    }
}