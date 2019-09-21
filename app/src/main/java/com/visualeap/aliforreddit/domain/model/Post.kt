package com.visualeap.aliforreddit.domain.model

data class Post(
    val id: String,
    val authorName: String,
    val title: String,
    val text: String,
    val score: Int,
    val commentCount: Int
)
