package com.visualeap.aliforreddit.presentation.common.formatter

import com.visualeap.aliforreddit.domain.post.Post
import com.visualeap.aliforreddit.domain.subreddit.Subreddit
import com.visualeap.aliforreddit.presentation.frontPage.PostDto

object PostFormatter {
    fun formatPost(subreddit: Subreddit, post: Post): PostDto {
        return PostDto(
            post.id,
            "u/${post.authorName}",
            post.title,
            post.text,
            formatCount(post.score),
            formatCount(post.commentCount),
            formatTimestamp(post.created),
            subreddit.id,
            "r/${subreddit.name}",
            SubredditFormatter.formatColor(subreddit.primaryColor, subreddit.keyColor),
            SubredditFormatter.formatIcon(subreddit.iconUrl)
        )
    }
}