package com.visualeap.aliforreddit.presentation.main.frontPage

import com.jakewharton.rxrelay2.BehaviorRelay
import com.visualeap.aliforreddit.SyncSchedulerProvider
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.repository.Listing
import com.visualeap.aliforreddit.domain.repository.NetworkState
import com.visualeap.aliforreddit.domain.repository.PostRepository
import com.visualeap.aliforreddit.util.mockPagedList
import io.mockk.*
import io.mockk.junit5.MockKExtension
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import util.domain.createPost

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class FrontPagePresenterTest {
    private val view: FrontPageView = mockk()
    private val repository: PostRepository = mockk()
    private lateinit var presenter: FrontPagePresenter

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
        presenter = FrontPagePresenter(view, repository, SyncSchedulerProvider())
    }

    @Nested
    inner class Start {
        @Test
        fun `pass a PagedList of post to view`() {
            //Arrange
            val pagedList = mockPagedList(listOf(createPost()))
            every { repository.getHomePosts(false) } returns Single.just(Listing(
                Observable.just(pagedList),
                BehaviorRelay.create()
            ) {})
            every { view.displayPosts(any()) } just runs

            //Act
            presenter.start()

            //Assert
            verify { view.displayPosts(pagedList) }
        }
    }
}