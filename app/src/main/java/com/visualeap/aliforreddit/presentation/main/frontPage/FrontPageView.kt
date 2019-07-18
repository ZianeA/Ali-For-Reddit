package com.visualeap.aliforreddit.presentation.main.frontPage

import com.visualeap.aliforreddit.domain.model.Post

interface FrontPageView {
    fun displayPosts(posts: List<Post>)
}
