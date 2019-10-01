package com.visualeap.aliforreddit.presentation.main.frontPage

import com.airbnb.epoxy.AsyncEpoxyController
import com.visualeap.aliforreddit.domain.model.Post
import android.R.id
import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.visualeap.aliforreddit.domain.model.Redditor
import com.visualeap.aliforreddit.domain.model.Subreddit
import com.visualeap.aliforreddit.presentation.util.IoSchedulerProvider
import java.util.concurrent.Executor


class FrontPageEpoxyController :
    PagedListEpoxyController<Post>(
        diffingHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler()
    ) {
    override fun buildItemModel(currentPosition: Int, item: Post?): EpoxyModel<*> {
        return PostEpoxyModel_()
            .id(item!!.id) //Placeholders are disabled
            .post(item)
    }
}