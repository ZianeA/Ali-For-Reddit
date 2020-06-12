package com.visualeap.aliforreddit.data.repository.post

import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditResponse
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.model.Subreddit
import dagger.Reusable
import javax.inject.Inject

@Reusable
class PostWithSubredditResponseMapper @Inject constructor(
    private val subredditMapper: @JvmSuppressWildcards Mapper<SubredditResponse, List<Subreddit>>
) :
    Mapper<PostWithSubredditResponse, List<@JvmSuppressWildcards Post>> {
    override fun mapReverse(model: List<Post>): PostWithSubredditResponse {
        /*val postHolders = mutableListOf<PostResponse.Data.PostHolder>()
        return model.forEach {
            postHolders.add(
                PostResponse.Data.PostHolder.Post(
                    it.id,
                    it.authorName,
                    it.title,
                    it.text,
                    it.score,
                    it.commentCount,
                    it.subreddit.id,
                    it.created
                )
            )
        }*/
        TODO("Method not needed")
    }

    override fun map(model: PostWithSubredditResponse): List<Post> {
        //map List<PostHolder> to List<Post>. And use the index to fetch the corresponding subreddit. This, of course, assumes that the two lists are in sync.
        //Or, I can use the find method. It's less performant but more practical.
        //I can also convert the list to a dictionary for faster lookup, however I'm not sure if the memory allocation is worth it.
        /*val subredditList = subredditMapper.map(model.subredditResponse)
        return model.postResponse.data.postHolders.map { postHolder ->
            postHolder.post.run {
                Post(
                    id,
                    authorName,
                    title,
                    text,
                    score,
                    commentCount,
                    subredditList.find { it.id == subredditId }!!,
                    created
                )
            }
        }*/

        TODO("Not Implemented")
    }
}