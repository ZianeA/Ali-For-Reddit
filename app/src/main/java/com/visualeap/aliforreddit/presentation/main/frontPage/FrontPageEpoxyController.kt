package com.visualeap.aliforreddit.presentation.main.frontPage

import com.airbnb.epoxy.AsyncEpoxyController
import com.visualeap.aliforreddit.domain.model.Post
import android.R.id


class FrontPageEpoxyController : AsyncEpoxyController() {
    var posts = listOf<Post>()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        posts.forEach {
            PostEpoxyModel_()
                .id(it.id)
                .title(it.title)
                .text(it.text)
                .authorName(it.authorName)
                .score(it.score.toString())
                .commentCount(it.commentCount.toString())
                .addTo(this)
        }
    }
}