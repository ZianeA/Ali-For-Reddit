package com.visualeap.aliforreddit.presentation.frontPage

import com.visualeap.aliforreddit.SyncSchedulerProvider
import com.visualeap.aliforreddit.domain.repository.SubmissionRepository
import io.reactivex.Observable
import net.dean.jraw.models.Submission
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.junit.Rule
import org.mockito.Mockito


class FrontPagePresenterTest {

    companion object {
        private val SUBMISSION_LIST = listOf(mock(Submission::class.java))
        private val SUBMISSION_LIST_OBSERVABLE = Observable.just(SUBMISSION_LIST)
    }

    @Rule
    @JvmField
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var view: FrontPageView

    @Mock
    private lateinit var repository: SubmissionRepository

    private lateinit var presenter: FrontPagePresenter

    @Before
    fun setUp() {
        presenter = FrontPagePresenter(view, repository, SyncSchedulerProvider())
    }

    @Test
    fun passSubmissionsToView() {
        //Arrange
        `when`(repository.getSubmissions()).thenReturn(SUBMISSION_LIST_OBSERVABLE)

        //Act
        presenter.loadSubmissions()

        //Assert
        verify(view, times(1)).displaySubmissions(SUBMISSION_LIST)
    }
}