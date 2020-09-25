package com.visualeap.aliforreddit.presentation.frontPage

data class PostDto(
    val id: String,
    val authorName: String,
    val title: String,
    val text: String?,
    val url: String?,
    val score: String,
    val commentCount: String,
    val timestamp: String,
    val subredditId: String,
    val subreddit: String,
    val subredditColor: String,
    val subredditIcon: SubredditIcon
)