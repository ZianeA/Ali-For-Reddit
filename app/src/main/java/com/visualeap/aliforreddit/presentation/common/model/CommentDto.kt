package com.visualeap.aliforreddit.presentation.common.model

data class CommentDto(
    val id: String,
    val authorName: String,
    val text: String,
    val score: String,
    val timestamp: String,
    val depth: Int,
    val postId: String,
    val parentId: String?,
    val replies: List<CommentDto>?,
    val isCollapsed: Boolean,
    val isLastReply: Boolean
)