package com.visualeap.aliforreddit.presentation.frontPage

import com.airbnb.epoxy.AsyncEpoxyController
import android.view.View
import com.airbnb.epoxy.SimpleEpoxyModel
import com.visualeap.aliforreddit.R


class FrontPageEpoxyController(
    private val onBindPostListener: (position: Int) -> Unit,
    private val onPostClickListener: ((clickedPost: PostDto) -> Unit)
) : AsyncEpoxyController() {
    var posts = listOf<PostDto>()
    var isLoading = false

    override fun buildModels() {
        posts.forEachIndexed { index, postDto ->
            post {
                id(postDto.id)
                post(postDto)
                bindListener { onBindPostListener(index) }
                clickListener(View.OnClickListener { onPostClickListener.invoke(postDto) })
                maxLines(POST_TEXT_MAX_LINES)
            }
        }

        SimpleEpoxyModel(R.layout.item_loading)
            .id("Loading Post")
            .addIf(isLoading, this)
    }

    companion object {
        private const val POST_TEXT_MAX_LINES = 3
    }
}