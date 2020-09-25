package com.visualeap.aliforreddit.data.post

object PostResponseMapper {
    fun map(response: PostResponse): List<Post> {
        return response.data.postHolders.map { postHolder ->
            postHolder.post.run {
                Post(id, authorName, title, text, url, score, commentCount, subredditId, created)
            }
        }
    }
}