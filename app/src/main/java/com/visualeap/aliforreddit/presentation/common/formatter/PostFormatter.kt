package com.visualeap.aliforreddit.presentation.common.formatter

import com.visualeap.aliforreddit.data.post.Post
import com.visualeap.aliforreddit.data.subreddit.Subreddit
import com.visualeap.aliforreddit.presentation.frontPage.PostDto

object PostFormatter {
    fun formatPost(subreddit: Subreddit, post: Post): PostDto {
        return PostDto(
            post.id,
            "u/${post.authorName}",
            post.title,
            post.text.takeIf { it.isNotBlank() },
            post.url.takeIf { it.endsWith("jpg") || it.endsWith("png") },
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