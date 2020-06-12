package com.visualeap.aliforreddit.util.fake

import com.visualeap.aliforreddit.data.repository.subreddit.SubredditResponse
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditResponse.Data.SubredditHolder.*
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditWebService
import com.visualeap.aliforreddit.domain.model.Subreddit
import io.reactivex.Single
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditResponse.Data.SubredditHolder.Subreddit as SubredditDto

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

    override fun getSubreddits(subredditIds: String): Single<SubredditResponse> {
        return Single.fromCallable {
            val subredditHolders = subredditIds.split(", ")
                .map { SubredditResponse.Data.SubredditHolder(subredditMap.getValue(it)) }

            SubredditResponse(SubredditResponse.Data(subredditHolders))
        }
    }
}