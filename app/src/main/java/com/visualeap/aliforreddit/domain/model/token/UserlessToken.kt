package com.visualeap.aliforreddit.domain.model.token

import com.squareup.moshi.Json

class UserlessToken(
    id: Int,
    accessToken: String,
    type: String,
    val deviceId: String
) : Token(id, accessToken, type) {
    fun copy(
        id: Int = this.id,
        accessToken: String = this.accessToken,
        type: String = this.type,
        deviceId: String = this.deviceId
    ) = UserlessToken(id, accessToken, type, deviceId)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserlessToken) return false
        if (!super.equals(other)) return false

        if (deviceId != other.deviceId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + deviceId.hashCode()
        return result
    }
}
