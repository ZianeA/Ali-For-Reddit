package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.entity.Token
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @FormUrlEncoded
    @POST(URL_PATH)
    fun getAccessToken(
        @Field("grant_type") grantType: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUrl: String,
        @Header("Authorization") credentials: String
    ): Single<Token>

    @FormUrlEncoded
    @POST(URL_PATH)
    fun getUserLessAccessToken(
        @Field("grant_type") grantType: String,
        @Field("device_id") deviceId: String,
        @Header("Authorization") credentials: String
    ): Single<Token>

    @FormUrlEncoded
    @POST(URL_PATH)
    fun refreshToken(
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String
    ): Single<Token>

    companion object{
        private const val URL_PATH = "api/v1/access_token"
    }
}