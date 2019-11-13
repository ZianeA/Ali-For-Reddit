package com.visualeap.aliforreddit.presentation.model

data class CommentView(
    val id: String,
    val authorName: String,
    val text: String,
    val score: String,
    val timestamp: String,
    val depth: Int,
    val postId: String,
    val parentId: String?,
    val replies: List<CommentView>?,
    val isCollapsed: Boolean,
    val isLastReply: Boolean
)