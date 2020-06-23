package com.visualeap.aliforreddit.presentation.frontPage

sealed class FrontPageViewState {
    object Loading : FrontPageViewState()
    data class Failure(val error: String) : FrontPageViewState()
    data class Success(val isLoading: Boolean, val posts: List<PostDto>) : FrontPageViewState()
}