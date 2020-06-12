package com.visualeap.aliforreddit.presentation.main.postDetail

import com.visualeap.aliforreddit.domain.model.Comment
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.presentation.model.CommentView
import com.visualeap.aliforreddit.presentation.model.PostView

interface PostDetailView {
    fun showPost(post: PostView)
    fun showComments(comments: List<CommentView>)
}
