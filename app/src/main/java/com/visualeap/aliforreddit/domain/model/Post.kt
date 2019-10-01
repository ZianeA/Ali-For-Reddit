package com.visualeap.aliforreddit.domain.model

data class Post(
    val id: String,
    val author: Redditor,
    val title: String,
    val text: String,
    val score: Int,
    val commentCount: Int,
    val subreddit: Subreddit,
    val created: Long
)
