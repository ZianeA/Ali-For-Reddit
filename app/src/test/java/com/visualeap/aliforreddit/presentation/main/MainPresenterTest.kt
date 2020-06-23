package com.visualeap.aliforreddit.presentation.main

import com.visualeap.aliforreddit.util.TrampolineSchedulerProvider
import com.visualeap.aliforreddit.domain.redditor.RedditorRepository
import com.visualeap.aliforreddit.domain.authentication.IsUserLoggedIn
import com.visualeap.aliforreddit.presentation.common.main.MainPresenter
import com.visualeap.aliforreddit.presentation.common.main.MainView
import io.mockk.*
import io.mockk.junit5.MockKExtension
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import util.domain.createRedditor

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class MainPresenterTest {
    private val repository: RedditorRepository = mockk()
    private val view: MainView = mockk(relaxed = true)
    private val isUserLoggedIn: IsUserLoggedIn = mockk()
    private val presenter =
        MainPresenter(
            view, repository, isUserLoggedIn,
            TrampolineSchedulerProvider()
        )

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `pass current redditor to view when user is logged-in`() {
        //Arrange
        val currentRedditor = createRedditor()
        every { isUserLoggedIn.execute() } returns Single.just(true)
        every { repository.getCurrentRedditor() } returns Single.just(currentRedditor)

        //Act
        presenter.start(true)

        //Assert
        verify { view.displayCurrentRedditor(currentRedditor) }
    }

    @Test
    fun `display login prompt when user is not logged-in`() {
        //Arrange
        every { isUserLoggedIn.execute() } returns Single.just(false)

        //Act
        presenter.start(true)

        //Assert
        verify { view.displayLoginPrompt() }
    }
}