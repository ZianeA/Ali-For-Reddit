package com.visualeap.aliforreddit.presentation.main.frontPage

import com.airbnb.epoxy.AsyncEpoxyController
import android.R.id
import android.view.View


class FrontPageEpoxyController(
    private val onBindPostListener: (position: Int) -> Unit,
    private val onPostClickListener: ((clickedPost: FeedPostDto) -> Unit)
) : AsyncEpoxyController() {
    var posts = listOf<FeedPostDto>()
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

        if (isLoading) { loadingPost { id("Loading Post") } }
    }

    companion object {
        private const val POST_TEXT_MAX_LINES = 3
    }
}