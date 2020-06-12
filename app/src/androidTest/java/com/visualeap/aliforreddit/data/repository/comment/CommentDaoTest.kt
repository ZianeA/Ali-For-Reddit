package com.visualeap.aliforreddit.data.repository.comment

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.visualeap.aliforreddit.data.database.RedditDatabase
import com.visualeap.aliforreddit.data.repository.post.PostDao
import io.reactivex.Completable
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import util.domain.*

@RunWith(AndroidJUnit4::class)
internal class CommentDaoTest {
    private lateinit var commentDao: CommentDao
    private lateinit var postDao: PostDao
    private lateinit var db: RedditDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getContext()
        db = Room.inMemoryDatabaseBuilder(context, RedditDatabase::class.java)
            .build()
        commentDao = db.commentDao()
        postDao = db.postDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetByPostId() {
        //Arrange
        val queriedPostId = "101"
        val otherPostId = "404"
        postDao.addAll(
            listOf(createSubredditEntity(), createSubredditEntity(name = "222", id = "111")),
            listOf(createPostEntity(id = queriedPostId), createPostEntity(id = otherPostId))
        )

        //Act, Assert
        val commentsFromTheSamePost = listOf(
            createCommentEntity(postId = queriedPostId),
            createCommentEntity(
                id = NESTED_COMMENT_ID,
                parentId = COMMENT_ID,
                depth = NESTED_COMMENT_DEPTH,
                postId = queriedPostId
            )
        )

        Completable.fromAction {
        }
            .andThen(
                commentDao.addAll(
                    listOf(
                        commentsFromTheSamePost[0],
                        commentsFromTheSamePost[1],
                        createCommentEntity(id = "404", postId = otherPostId)
                    )
                )
            )
            .andThen(commentDao.getByPostId(queriedPostId))
            .test()
            .assertResult(commentsFromTheSamePost)
    }
}