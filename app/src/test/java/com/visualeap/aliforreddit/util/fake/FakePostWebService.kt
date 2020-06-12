package com.visualeap.aliforreddit.util.fake

import com.visualeap.aliforreddit.data.repository.post.PostResponse
import com.visualeap.aliforreddit.data.repository.post.PostResponse.Data.PostHolder.*
import com.visualeap.aliforreddit.data.repository.post.PostWebService
import com.visualeap.aliforreddit.domain.model.Post
import io.reactivex.Single
import kotlin.math.min
import com.visualeap.aliforreddit.data.repository.post.PostResponse.Data.PostHolder.Post as PostDto

class FakePostWebService : PostWebService {
    private val subredditToPosts = mutableMapOf<String, MutableList<PostDto>>()

    fun addPosts(subredditName: String, posts: List<Post>) {
        this.subredditToPosts.getOrPut(subredditName) { mutableListOf() }
            .addAll(posts.map {
                PostDto(
                    it.id,
                    it.authorName,
                    it.title,
                    it.text,
                    it.score,
                    it.commentCount,
                    it.subredditId,
                    it.created
                )
            })
    }

    fun addPost(SubredditName: String, post: Post) = addPosts(SubredditName, listOf(post))

    override fun getPostsBySubreddit(
        subredditName: String,
        limit: Int,
        after: String?
    ): Single<PostResponse> {
        return Single.fromCallable {
            val posts = subredditToPosts.getOrPut(subredditName) { mutableListOf() }
            val fromIndex = after?.toIntOrNull() ?: 0
            val toIndex = min(posts.size, fromIndex + limit)
            val afterKey = (fromIndex + limit).takeIf { it < posts.size }

            PostResponse(
                PostResponse.Data(
                    afterKey?.toString(),
                    posts.subList(0, toIndex).map { PostResponse.Data.PostHolder(it) }
                )
            )
        }
    }

    override fun getHomePosts(limit: Int, after: String?): Single<PostResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}