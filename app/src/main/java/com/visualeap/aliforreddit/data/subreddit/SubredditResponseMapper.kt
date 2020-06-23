package com.visualeap.aliforreddit.data.subreddit

import com.visualeap.aliforreddit.domain.subreddit.Subreddit

object SubredditResponseMapper {
    fun map(response: SubredditResponse): List<Subreddit> {
        return response.data.subredditHolders.map { subredditHolder ->
            subredditHolder.subreddit.run {
                Subreddit(
                    id,
                    name,
                    iconUrl,
                    primaryColor?.takeIf { it.isNotBlank() },
                    keyColor?.takeIf { it.isNotBlank() }
                )
            }
        }
    }
}