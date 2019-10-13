package com.visualeap.aliforreddit.data.repository.post

import com.visualeap.aliforreddit.data.repository.redditor.RedditorResponse
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditResponse

data class PostWithSubredditResponse(
    val postResponse: PostResponse,
    val subredditResponse: SubredditResponse
)