package com.visualeap.aliforreddit.presentation.mapper

import com.visualeap.aliforreddit.domain.model.Subreddit
import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.presentation.model.SubredditView
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SubredditViewMapper @Inject constructor() : Mapper<SubredditView, Subreddit> {
    override fun map(model: SubredditView): Subreddit {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mapReverse(model: Subreddit): SubredditView = model.run {
        SubredditView("r/$name", id, iconUrl, primaryColor ?: keyColor ?: DEFAULT_COLOR)
    }

    companion object {
        private const val DEFAULT_COLOR = "#33a8ff"
    }
}