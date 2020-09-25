package com.visualeap.aliforreddit.data.subreddit

object SubredditResponseMapper {
    fun map(response: SubredditResponse): List<Subreddit> {
        return response.data.subredditHolders.map { subredditHolder ->
            subredditHolder.subreddit.run {
                Subreddit(
                    id,
                    name,
                    iconUrl?.takeIf { it.isNotBlank() },
                    primaryColor?.takeIf { it.isNotBlank() },
                    keyColor?.takeIf { it.isNotBlank() }
                )
            }
        }
    }
}