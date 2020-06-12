package com.visualeap.aliforreddit.presentation.main.frontPage

sealed class FrontPageViewState {
    object Loading : FrontPageViewState()
    object Failure: FrontPageViewState()
    data class Success(val isLoading: Boolean, val posts: List<FeedPostDto>) : FrontPageViewState()
}