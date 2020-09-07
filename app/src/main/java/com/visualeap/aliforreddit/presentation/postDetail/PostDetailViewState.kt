package com.visualeap.aliforreddit.presentation.postDetail

import com.visualeap.aliforreddit.domain.comment.Comment
import com.visualeap.aliforreddit.presentation.frontPage.PostDto

data class PostDetailViewState(
    val post: PostDto? = null,
    val comments: List<Comment>? = null,
    val postLoading: Boolean = false,
    val postError: String? = null,
    val commentsLoading: Boolean = false,
    val commentsError: String? = null
)

sealed class PostDetailEvent {
    object ScreenLoadEvent : PostDetailEvent()
}

sealed class PostDetailResult {
    sealed class ScreenLoadResult : PostDetailResult() {
        data class PostContent(val post: PostDto) : ScreenLoadResult()
        data class CommentsContent(val comments: List<Comment>) : ScreenLoadResult()
        data class PostError(val error: String) : ScreenLoadResult()
        data class CommentsError(val error: String) : ScreenLoadResult()
        object PostLoading : ScreenLoadResult()
        object CommentsLoading : ScreenLoadResult()
    }
}
