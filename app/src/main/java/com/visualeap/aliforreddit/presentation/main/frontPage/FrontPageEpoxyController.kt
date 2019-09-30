package com.visualeap.aliforreddit.presentation.main.frontPage

import com.airbnb.epoxy.AsyncEpoxyController
import com.visualeap.aliforreddit.domain.model.Post
import android.R.id
import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.visualeap.aliforreddit.domain.model.Redditor
import com.visualeap.aliforreddit.domain.model.Subreddit


class FrontPageEpoxyController :
    PagedListEpoxyController<Post>(
        /*modelBuildingHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler(),
        diffingHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler()*/
    ) {
    override fun buildItemModel(currentPosition: Int, item: Post?): EpoxyModel<*> {
        return if (item == null) {
            //TODO deal with this
            val placeHolder = Post(
                "101",
                Redditor("PlaceHolderUsername"),
                "PlaceHolder Title",
                "PlaceHolder Text",
                69,
                69,
                Subreddit("202", "PlaceHolder Subreddit")
            )
            PostEpoxyModel_()
                .id(-currentPosition)
                .post(placeHolder)
        } else {
            PostEpoxyModel_()
                .id(item.id)
                .post(item)
        }
    }

    /*var posts = listOf<Post>()
        set(value) {
            field = value
            requestModelBuild()
        }*/

    /*override fun buildModels() {
        posts.forEach {
            PostEpoxyModel_()
                .id(it.id)
                .title(it.title)
                .text(it.text)
                *//*.authorName(it.authorName)*//*
                .score(it.score.toString())
                .commentCount(it.commentCount.toString())
                .addTo(this)
        }
    }*/
}