package com.visualeap.aliforreddit.presentation.main.frontPage

import com.airbnb.epoxy.AsyncEpoxyController
import com.visualeap.aliforreddit.domain.model.Post
import android.R.id
import android.view.View
import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.visualeap.aliforreddit.domain.model.Redditor
import com.visualeap.aliforreddit.domain.model.Subreddit
import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.presentation.model.PostView
import com.visualeap.aliforreddit.presentation.util.IoSchedulerProvider
import java.util.concurrent.Executor


class FrontPageEpoxyController(
    private val postViewMapper: Mapper<PostView, Post>,
    private val onPostClickListener: ((clickedPost: PostView) -> Unit)
) :
    PagedListEpoxyController<Post>(
        diffingHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler()
    ) {
    override fun buildItemModel(currentPosition: Int, item: Post?): EpoxyModel<*> {
        val postView = postViewMapper.mapReverse(item!!) //Placeholders are disabled
        return PostEpoxyModel_()
            .id(postView.id)
            .post(postView)
            .listener(View.OnClickListener { onPostClickListener.invoke(postView) })
            .maxLines(POST_TEXT_MAX_LINES)
    }

    companion object{
        private const val POST_TEXT_MAX_LINES = 3
    }
}