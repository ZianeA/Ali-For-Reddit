package com.visualeap.aliforreddit.data.repository.subreddit

import com.visualeap.aliforreddit.data.repository.Mapper
import com.visualeap.aliforreddit.domain.model.Subreddit
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SubredditResponseMapper @Inject constructor() :
    Mapper<SubredditResponse, List<@JvmSuppressWildcards Subreddit>> {
    override fun map(model: SubredditResponse): List<Subreddit> {
        return model.data.subredditHolders.map { subredditHolder ->
            subredditHolder.subreddit.run {
                Subreddit(
                    name,
                    id,
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