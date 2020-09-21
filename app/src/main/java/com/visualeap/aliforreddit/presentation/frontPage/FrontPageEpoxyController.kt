package com.visualeap.aliforreddit.presentation.frontPage

import com.airbnb.epoxy.AsyncEpoxyController
import android.view.View
import com.airbnb.epoxy.SimpleEpoxyModel
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.common.util.EpoxyAutoBuild


class FrontPageEpoxyController : AsyncEpoxyController() {
    var posts: List<PostDto> by EpoxyAutoBuild(this, emptyList())
    var isLoadingMore : Boolean by EpoxyAutoBuild(this, false)
    var onBindPostListener: ((position: Int) -> Unit)? = null
    var onPostClickListener: ((clickedPost: PostDto) -> Unit)? = null

    override fun buildModels() {
        posts.forEachIndexed { index, postDto ->
            post {
                id(postDto.id)
                post(postDto)
                bindListener { onBindPostListener?.invoke(index) }
                clickListener(View.OnClickListener { onPostClickListener?.invoke(postDto) })
                maxLines(POST_TEXT_MAX_LINES)
            }
        }

        SimpleEpoxyModel(R.layout.item_loading)
            .id("Loading Post")
            .addIf(isLoadingMore, this)
    }

    companion object {
        private const val POST_TEXT_MAX_LINES = 3
    }
}