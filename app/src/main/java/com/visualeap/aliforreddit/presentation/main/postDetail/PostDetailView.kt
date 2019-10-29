package com.visualeap.aliforreddit.presentation.main.postDetail

import androidx.paging.PagedList
import com.visualeap.aliforreddit.domain.model.Comment
import com.visualeap.aliforreddit.domain.model.Post

interface PostDetailView {
    fun showPost(post: Post)
    fun showComments(comments: List<Comment>)
}
