package com.visualeap.aliforreddit.domain.comment

data class Comment(
    val id: String,
    val authorName: String,
    val text: String,
    val score: Int,
    val creationDate: Long,
    val depth: Int,
    val postId: String,
    val parentId: String?,
    val replies: List<Comment>?
)