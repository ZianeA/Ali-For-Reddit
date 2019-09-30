package com.visualeap.aliforreddit.data.repository.post

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import androidx.paging.toObservable
import com.visualeap.aliforreddit.data.network.RedditService
import com.visualeap.aliforreddit.data.repository.Mapper
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.repository.PostRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostDataRepository @Inject constructor(
    private val postDao: PostDao,
    private val postBoundaryCallback: PostBoundaryCallback,
    private val postWithRedditorMapper: Mapper<PostWithRedditor, Post>
) :
    PostRepository {
    override fun getPostsBySubreddit(subreddit: String): Observable<PagedList<Post>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHomePosts(): Observable<PagedList<Post>> {
        val postFactory = postDao.getAll().map(postWithRedditorMapper::map)
        val config = PagedList.Config.Builder()
            .setPageSize(DATABASE_PAGE_SIZE)
            .setEnablePlaceholders(true)
            .build()
        return RxPagedListBuilder(postFactory, config)
            .setBoundaryCallback(postBoundaryCallback)
            .buildObservable()

        /*return postDao.getAll()
            .map(postWithRedditorMapper::map)
            .toObservable(pageSize = DATABASE_PAGE_SIZE, boundaryCallback = postBoundaryCallback)*/
    }

    companion object {
        //TODO change to 25
        private const val DATABASE_PAGE_SIZE = 5
    }
}