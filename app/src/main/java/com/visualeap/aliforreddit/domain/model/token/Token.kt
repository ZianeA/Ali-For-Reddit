package com.visualeap.aliforreddit.domain.model.token

open class Token(val id: Int, val accessToken: String, val type: String) {
    fun copy(
        id: Int = this.id,
        accessToken: String = this.accessToken,
        type: String = this.type
    ) = Token(id, accessToken, type)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Token

        if (id != other.id) return false
        if (accessToken != other.accessToken) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + accessToken.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}