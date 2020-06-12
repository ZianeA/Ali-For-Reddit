package com.visualeap.aliforreddit.domain.model

sealed class AfterKey {
    data class Next(val value: String): AfterKey()
    object End : AfterKey()
    object Empty : AfterKey()
}