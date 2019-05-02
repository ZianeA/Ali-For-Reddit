package com.visualeap.aliforreddit.data

import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface RedditService {
    @FormUrlEncoded
    @POST("api/v1/access_token")
    fun getAccessToken(
        @Field("grant_type") grantType: String,
        @Field("device_id") deviceId: String,
        @Header("Authorization") credentials: String
    ): Observable<AccessToken>
}