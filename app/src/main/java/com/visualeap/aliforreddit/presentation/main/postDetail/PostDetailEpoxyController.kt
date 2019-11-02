package com.visualeap.aliforreddit.presentation.main.postDetail

import android.view.View
import com.airbnb.epoxy.AsyncEpoxyController
import com.visualeap.aliforreddit.domain.model.Comment
import com.visualeap.aliforreddit.presentation.main.frontPage.PostEpoxyModel_
import com.visualeap.aliforreddit.presentation.model.PostView

class PostDetailEpoxyController(private val post: PostView) : AsyncEpoxyController() {
    var comments: List<Comment> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        PostEpoxyModel_()
            .id(post.id)
            .post(post)
            .listener(View.OnClickListener {  }) //TODO refactor
            .maxLines(Int.MAX_VALUE)
            .addTo(this)

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