package com.visualeap.aliforreddit.data.subreddit

import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.domain.subreddit.Subreddit
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SubredditResponseMapper @Inject constructor() :
    Mapper<SubredditResponse, List<@JvmSuppressWildcards Subreddit>> {
    override fun map(model: SubredditResponse): List<Subreddit> {
        return model.data.subredditHolders.map { subredditHolder ->
            subredditHolder.subreddit.run {
                Subreddit(
                    id,
                    name,
                    iconUrl,
                    if (primaryColor.isNullOrEmpty() || primaryColor.isBlank()) null else primaryColor, //TODO Unit test or refactor
                    if (keyColor.isNullOrEmpty() || keyColor.isBlank()) null else keyColor
                )
            }
        }
    }

    override fun mapReverse(model: List<Subreddit>): SubredditResponse {
        TODO("not needed") //To change body of created functions use File | Settings | File Templates.
    }
}