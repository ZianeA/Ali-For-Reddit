package com.visualeap.aliforreddit.domain.post

sealed class AfterKey {
    data class Next(val value: String) : AfterKey()
    object End : AfterKey()
    object Empty : AfterKey()

    fun toStringOrNull() = if (this is Next) this.value else null
}