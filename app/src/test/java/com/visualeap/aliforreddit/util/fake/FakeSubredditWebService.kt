package com.visualeap.aliforreddit.util.fake

import com.visualeap.aliforreddit.data.subreddit.Subreddit
import com.visualeap.aliforreddit.data.subreddit.SubredditResponse
import com.visualeap.aliforreddit.data.subreddit.SubredditResponse.Data.SubredditHolder.*
import com.visualeap.aliforreddit.data.subreddit.SubredditWebService
import io.reactivex.Single
import com.visualeap.aliforreddit.data.subreddit.SubredditResponse.Data.SubredditHolder.Subreddit as SubredditDto

class FakeSubredditWebService : SubredditWebService {
    private val subredditMap = mutableMapOf<String, SubredditDto>()

    fun addSubreddits(subreddits: List<Subreddit>) {
        subreddits.forEach { subreddit ->
            subredditMap[subreddit.id] = SubredditDto(
                subreddit.id,
                subreddit.name,
                subreddit.iconUrl,
                subreddit.primaryColor,
                subreddit.keyColor
            )
        }
    }

    fun addSubreddit(subreddit: Subreddit) = addSubreddits(listOf(subreddit))

    override fun getSubredditsByIds(subredditIds: String): Single<SubredditResponse> {
        return Single.fromCallable {
            val subredditHolders = subredditIds.split(", ")
                .map { SubredditResponse.Data.SubredditHolder(subredditMap.getValue(it)) }

            SubredditResponse(SubredditResponse.Data(subredditHolders))
        }
    }
}