package com.visualeap.aliforreddit.presentation.main.frontPage

import androidx.paging.PagedList
import com.visualeap.aliforreddit.domain.model.Post

interface FrontPageView {
    fun displayPosts(posts: PagedList<Post>)
}
