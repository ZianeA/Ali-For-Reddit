package com.visualeap.aliforreddit.domain.model

data class Subreddit(
    val name: String,
    val id: String,
    val iconUrl: String?,
    val primaryColor: String?,
    val keyColor: String?
)
