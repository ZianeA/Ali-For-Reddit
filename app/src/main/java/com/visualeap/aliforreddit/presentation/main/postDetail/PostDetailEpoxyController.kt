package com.visualeap.aliforreddit.presentation.main.postDetail

import com.airbnb.epoxy.AsyncEpoxyController
import com.visualeap.aliforreddit.domain.model.Comment

class PostDetailEpoxyController : AsyncEpoxyController() {
    var comments: List<Comment> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        buildCommentModelsTree(comments)
    }

    private fun buildCommentModelsTree(commentList: List<Comment>){
        commentList.forEach {
            CommentEpoxyModel_()
                .id(it.id)
                .comment(it)
                .addTo(this)

            if(it.replies != null){
                buildCommentModelsTree(it.replies)
            }
        }
    }
}