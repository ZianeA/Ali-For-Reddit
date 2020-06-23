package com.visualeap.aliforreddit.domain.redditor

data class Redditor(
    val id: String,
    val username: String,
    val creationDate: Long,
    val linkKarma: Int,
    val commentKarma: Int,
    val iconUrl: String,
    val coins: Int
)
