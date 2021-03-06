package com.visualeap.aliforreddit.presentation.frontPage.container

import com.visualeap.aliforreddit.util.TrampolineSchedulerProvider
import com.visualeap.aliforreddit.domain.authentication.IsUserLoggedIn
import io.mockk.*
import io.mockk.junit5.MockKExtension
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class FrontPageContainerPresenterTest {
    private val view: FrontPageContainerView = mockk(relaxed = true)
    private val isUserLoggedIn: IsUserLoggedIn = mockk()
    private val presenter =
        FrontPageContainerPresenter(view, isUserLoggedIn,
            TrampolineSchedulerProvider()
        )

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `show login screen when user is not logged in`() {
        //Arrange
        every { isUserLoggedIn.execute() } returns Single.just(false)

        //Act
        presenter.start()

        //Assert
        verify { view.showLoginScreen() }
    }

    @Test
    fun `show home screen when user is logged in`() {
        //Arrange
        every { isUserLoggedIn.execute() } returns Single.just(true)

        //Act
        presenter.start()

        //Assert
        verify { view.showHomeScreen() }
    }
}