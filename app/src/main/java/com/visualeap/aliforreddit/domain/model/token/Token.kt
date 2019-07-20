package com.visualeap.aliforreddit.domain.model.token

open class Token(val id: Int, val accessToken: String, val type: String) {
    fun copy(
        id: Int = this.id,
        accessToken: String = this.accessToken,
        type: String = this.type
    ) = Token(id, accessToken, type)
}