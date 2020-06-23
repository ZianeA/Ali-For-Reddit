package com.visualeap.aliforreddit.presentation.postDetail

import com.visualeap.aliforreddit.presentation.common.model.CommentDto
import com.visualeap.aliforreddit.presentation.frontPage.PostDto

data class PostDetailViewState(
    val post: PostDto? = null,
    val comments: List<CommentDto>? = null,
    val postLoading: Boolean = false,
    val postError: String? = null,
    val commentsLoading: Boolean = false,
    val commentsError: String? = null
)

sealed class PostDetailEvent {
    object ScreenLoadEvent : PostDetailEvent()
    object CollapseCommentEvent : PostDetailEvent()
}

sealed class PostDetailResult {
    sealed class ScreenLoadResult : PostDetailResult() {
        data class PostContent(val post: PostDto) : ScreenLoadResult()
        data class CommentsContent(val comments: List<CommentDto>) : ScreenLoadResult()
        data class PostError(val error: String) : ScreenLoadResult()
        data class CommentsError(val error: String) : ScreenLoadResult()
        object PostLoading : ScreenLoadResult()
        object CommentsLoading : ScreenLoadResult()
    }

    object CollapseCommentResult : PostDetailResult()
}
/*{
    object Loading : PostDetailViewState()

    data class Failure(val error: String) : PostDetailViewState()

    data class PostSuccess(val post: PostDto, val commentError: String?) : PostDetailViewState()

    data class CommentSuccess(val comments: List<CommentDto>, val postError: String?) :
        PostDetailViewState()

    data class Success(val post: PostDto, val comments: List<CommentDto>) : PostDetailViewState()
}*/
