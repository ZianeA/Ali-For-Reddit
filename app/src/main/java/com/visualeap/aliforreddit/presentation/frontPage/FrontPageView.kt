package com.visualeap.aliforreddit.presentation.frontPage

import com.visualeap.aliforreddit.domain.entity.Post

interface FrontPageView {
    fun displayPosts(posts: List<Post>)
}
