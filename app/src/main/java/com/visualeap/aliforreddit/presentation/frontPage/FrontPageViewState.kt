package com.visualeap.aliforreddit.presentation.frontPage

data class FrontPageViewState(
    val posts: List<PostDto> = emptyList(),
    val loading: Boolean = false,
    val loadingMore: Boolean = false,
    val error: String? = null
)

sealed class FrontPageEvent {
    object ScreenLoadEvent : FrontPageEvent()
    data class PostBoundEvent(val position: Int, val isScrollingDown: Boolean) : FrontPageEvent()
}

sealed class FrontPageResult {
    sealed class ScreenLoadResult : FrontPageResult() {
        data class Content(val posts: List<PostDto>, val loadingMore: Boolean) :
            ScreenLoadResult()

        data class Error(val error: String) : ScreenLoadResult()
        object Loading : ScreenLoadResult()
    }

    object PostBoundResult : FrontPageResult()
}