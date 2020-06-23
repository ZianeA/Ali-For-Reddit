package com.visualeap.aliforreddit.data.post

import com.visualeap.aliforreddit.data.subreddit.SubredditResponse

data class PostWithSubredditResponse(
    val postResponse: PostResponse,
    val subredditResponse: SubredditResponse
)