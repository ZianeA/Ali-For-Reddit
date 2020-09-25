package com.visualeap.aliforreddit.util.fake

import com.visualeap.aliforreddit.data.post.Post
import com.visualeap.aliforreddit.data.post.PostResponse
import com.visualeap.aliforreddit.data.post.PostResponse.Data.PostHolder.*
import com.visualeap.aliforreddit.data.post.PostWebService
import io.reactivex.Single
import java.io.IOException
import kotlin.math.min
import com.visualeap.aliforreddit.data.post.PostResponse.Data.PostHolder.Post as PostDto

class FakePostWebService : PostWebService {
    private val subredditToPosts = mutableMapOf<String, MutableList<PostDto>>()
    private var error = false

    fun addPosts(subreddit: String, posts: List<Post>) {
        this.subredditToPosts.getOrPut(subreddit) { mutableListOf() }
            .addAll(posts.map {
                PostDto(
                    it.id,
                    it.authorName,
                    it.title,
                    it.text,
                    it.url,
                    it.score,
                    it.commentCount,
                    it.subredditId,
                    it.created
                )
            })
    }

    fun addPost(SubredditName: String, post: Post) = addPosts(SubredditName, listOf(post))

    fun simulateError() {
        error = true
    }

    override fun getPostsByIds(postId: String): Single<PostResponse> {
        if (error) return Single.error(IOException())

        return Single.fromCallable {
            val postHolders = postId.split(", ")
                .map { id ->
                    PostResponse.Data.PostHolder(
                        subredditToPosts.values.flatten().find { it.id == id }!!
                    )
                }

            PostResponse(PostResponse.Data(null, postHolders))
        }
    }

    override fun getPostsBySubreddit(
        subredditName: String,
        limit: Int,
        after: String?
    ): Single<PostResponse> {
        if (error) return Single.error(IOException())

        return Single.fromCallable {
            val posts = subredditToPosts.getOrPut(subredditName) { mutableListOf() }
            val fromIndex = after?.toIntOrNull() ?: 0
            val toIndex = min(posts.size, fromIndex + limit)
            val afterKey = (fromIndex + limit).takeIf { it < posts.size }

            PostResponse(
                PostResponse.Data(
                    afterKey?.toString(),
                    posts.subList(fromIndex, toIndex).map { PostResponse.Data.PostHolder(it) }
                )
            )
        }
    }

    override fun getHomePosts(limit: Int, after: String?): Single<PostResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}