package com.visualeap.aliforreddit.domain.model.token

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


class UserToken(
    id: Int,
    accessToken: String,
    type: String,
    val refreshToken: String
) : Token(id, accessToken, type) {
    fun copy(
        id: Int = this.id,
        accessToken: String = this.accessToken,
        type: String = this.type,
        refreshToken: String = this.refreshToken
    ) = UserToken(id, accessToken, type, refreshToken)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as UserToken

        if (refreshToken != other.refreshToken) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + refreshToken.hashCode()
        return result
    }


}