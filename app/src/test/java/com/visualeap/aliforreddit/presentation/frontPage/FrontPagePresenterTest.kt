package com.visualeap.aliforreddit.presentation.frontPage

import com.visualeap.aliforreddit.SyncSchedulerProvider
import com.visualeap.aliforreddit.domain.entity.Post
import com.visualeap.aliforreddit.domain.repository.PostRepository
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.junit.Rule

class FrontPagePresenterTest {

    companion object {
        private val POSTS_LIST = listOf(Post())
        private val POST_LIST_OBSERVABLE = Observable.just(POSTS_LIST)
    }

    @Rule
    @JvmField
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var view: FrontPageView

    @Mock
    private lateinit var repository: PostRepository

    private lateinit var presenter: FrontPagePresenter

    @Before
    fun setUp() {
        presenter = FrontPagePresenter(view, repository, SyncSchedulerProvider())
    }

    @Test
    fun passSubmissionsToView() {
        //Arrange
        `when`(repository.getPosts()).thenReturn(POST_LIST_OBSERVABLE)

        //Act
        presenter.loadSubmissions()

        //Assert
        verify(view, times(1)).displayPosts(POSTS_LIST)
    }
}