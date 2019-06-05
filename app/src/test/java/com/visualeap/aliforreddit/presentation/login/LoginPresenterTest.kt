package com.visualeap.aliforreddit.presentation.login

import com.visualeap.aliforreddit.domain.usecase.*
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class LoginPresenterTest {

    companion object {
        private const val REDIRECT_URL = "https://example.com/path"
        private const val AUTH_URL = "https://www.reddit.com/api/v1/authorize"
        private const val CLIENT_ID = "CLIENT ID"
        private const val STATE = "STATE"
        private const val FINAL_REDIRECT_URL = "$REDIRECT_URL?state=$STATE"
    }

    private val view: LoginView = mockk(relaxed = true)
    private val getUniqueString: GetUniqueString = mockk(relaxed = true)
    private val getAuthUrl: GetAuthUrl = mockk()
    private val isFinalRedirectUrl: IsFinalRedirectUrl = mockk()
    private val authenticateUser: AuthenticateUser = mockk(relaxed = true)

    private val presenter = LoginPresenter(
        view,
        getUniqueString,
        getAuthUrl,
        isFinalRedirectUrl,
        authenticateUser
    )

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Nested
    inner class Start {

        @Test
        fun `pass auth url to view`() {
            //Arrange
            every { getUniqueString.execute(Unit) } returns STATE
            every { getAuthUrl.execute(STATE) } returns AUTH_URL

            //Act
            presenter.start()

            //Assert
            verify { view.showLoginPage(AUTH_URL) }
        }
    }

    @Nested
    inner class OnPageStarted {

        @Test
        fun `hide login ui when authorization is complete`() {
            //Arrange
            every { isFinalRedirectUrl.execute(FINAL_REDIRECT_URL) } returns true

            //Act
            presenter.onPageStarted(FINAL_REDIRECT_URL)

            //Assert
            verify { view.hideLoginPage() }
        }
    }
}