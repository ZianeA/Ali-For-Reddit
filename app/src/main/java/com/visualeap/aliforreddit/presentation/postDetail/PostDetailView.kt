package com.visualeap.aliforreddit.presentation.postDetail

import com.visualeap.aliforreddit.presentation.common.model.CommentView
import com.visualeap.aliforreddit.presentation.common.model.PostView

interface PostDetailView {
    fun showPost(post: PostView)
    fun showComments(comments: List<CommentView>)
}
