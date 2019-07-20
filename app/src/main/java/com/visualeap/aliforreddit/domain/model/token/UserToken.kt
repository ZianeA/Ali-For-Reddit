package com.visualeap.aliforreddit.domain.model.token

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/*@JsonClass(generateAdapter = true)*/
class UserToken(
    id: Int,
    /*@Json(name = "access_token")*/ accessToken: String,
    /*@Json(name = "token_type")*/ type: String,
    /*@Json(name = "refresh_token")*/ val refreshToken: String? = null
) : Token(id, accessToken, type) {
    fun copy(
        id: Int = this.id,
        accessToken: String = this.accessToken,
        type: String = this.type,
        refreshToken: String? = this.refreshToken
    ) = UserToken(id, accessToken, type, refreshToken)
}