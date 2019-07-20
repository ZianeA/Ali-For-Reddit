package com.visualeap.aliforreddit.domain.model.token

import com.squareup.moshi.Json

class UserlessToken(
    id: Int,
    accessToken: String,
    type: String,
    val deviceId: String? //TODO make deviceId non-nullable
) : Token(id, accessToken, type) {
    fun copy(
        id: Int = this.id,
        accessToken: String = this.accessToken,
        type: String = this.type,
        deviceId: String? = this.deviceId
    ) = UserlessToken(id, accessToken, type, deviceId)
}
