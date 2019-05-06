package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.entity.Token
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @FormUrlEncoded
    @POST("api/v1/access_token")
    fun getAccessToken(
        @Field("grant_type") grantType: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUrl: String
    ): Observable<Token>

    @FormUrlEncoded
    @POST("api/v1/access_token")
    fun getUserLessAccessToken(
        @Field("grant_type") grantType: String,
        @Field("device_id") deviceId: String,
        @Header("Authorization") credentials: String
    ): Observable<Token>
}