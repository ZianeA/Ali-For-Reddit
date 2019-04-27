package com.visualeap.aliforreddit.presentation.frontPage

import com.visualeap.aliforreddit.domain.entity.Submission
import com.visualeap.aliforreddit.domain.repository.SubmissionRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.junit.Rule


class FrontPagePresenterTest {

    companion object {
        private val SUBMISSION_LIST = listOf(Submission())
    }

    @Rule
    @JvmField
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var view: FrontPageView

    @Mock
    private lateinit var repository: SubmissionRepository

    @InjectMocks
    private lateinit var presenter: FrontPagePresenter

    @Test
    fun passSubmissionsToView() {
        //Arrange
        `when`(repository.getSubmissions()).thenReturn(SUBMISSION_LIST)

        //Act
        presenter.loadSubmissions()

        //Assert
        verify(view).displaySubmissions(SUBMISSION_LIST)
    }
}