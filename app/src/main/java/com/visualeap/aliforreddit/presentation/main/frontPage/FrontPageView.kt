package com.visualeap.aliforreddit.presentation.main.frontPage

import androidx.paging.PagedList
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.presentation.model.PostView

interface FrontPageView {
    fun displayPosts(posts: PagedList<Post>)
}
