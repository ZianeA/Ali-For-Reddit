package com.visualeap.aliforreddit.presentation.frontPage

data class FeedPostDto(
    val id: String,
    val authorName: String,
    val title: String,
    val text: String,
    val score: String,
    val commentCount: String,
    val timestamp: String,
    val subredditId: String,
    val subreddit: String,
    val subredditColor: String,
    val subredditIcon: SubredditIcon
)