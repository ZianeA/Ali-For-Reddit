package com.visualeap.aliforreddit.presentation.main.frontPage

import com.airbnb.epoxy.AsyncEpoxyController
import com.visualeap.aliforreddit.domain.model.Post
import android.R.id
import android.view.View
import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.EpoxyModel
import com.visualeap.aliforreddit.domain.model.Redditor
import com.visualeap.aliforreddit.domain.model.Subreddit
import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.presentation.model.PostView
import com.visualeap.aliforreddit.presentation.util.IoSchedulerProvider
import java.util.concurrent.Executor


class FrontPageEpoxyController(
    private val onPostClickListener: ((clickedPost: FeedPostDto) -> Unit)
) : AsyncEpoxyController() {
    var posts = listOf<FeedPostDto>()

    override fun buildModels() {
        posts.forEachIndexed { index, postDto ->
            post {
                id(postDto.id)
                post(postDto)
                clickListener(View.OnClickListener { onPostClickListener.invoke(postDto) })
                maxLines(POST_TEXT_MAX_LINES)
            }
        }
    }

    companion object {
        private const val POST_TEXT_MAX_LINES = 3
    }
}