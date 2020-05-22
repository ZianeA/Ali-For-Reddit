package com.visualeap.aliforreddit.presentation.login

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.SyncSchedulerProvider
import com.visualeap.aliforreddit.domain.usecase.*
import com.visualeap.aliforreddit.domain.util.UniqueStringGenerator
import com.visualeap.aliforreddit.presentation.common.ResourceProvider
import com.visualeap.aliforreddit.presentation.main.login.LoginPresenter
import com.visualeap.aliforreddit.presentation.main.login.LoginView
import io.mockk.*
import io.mockk.junit5.MockKExtension
import io.reactivex.Completable
import okhttp3.HttpUrl
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
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
        private const val CLIENT_ID = "CLIENT_ID"
        private const val FINAL_REDIRECT_URL = "$REDIRECT_URL?key=value"
    }

    private val view: LoginView = mockk(relaxed = true)
    private val buildAuthUrl: BuildAuthUrl = mockk(relaxed = true)
    private val authenticateUser: AuthenticateUser = mockk(relaxed = true)
    private val resourceProvider: ResourceProvider = mockk(relaxed = true)
    private val presenter = LoginPresenter(
        view,
        buildAuthUrl,
        authenticateUser,
        resourceProvider,
        SyncSchedulerProvider()
    )

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()

        // Set defaults
        every { resourceProvider.getString(R.string.redirect_url) } returns REDIRECT_URL
        every { resourceProvider.getString(R.string.client_id) } returns CLIENT_ID
    }

    @Nested
    inner class OnLogInClicked {
        @Test
        fun `pass auth url to view`() {
            //Arrange
            val authUrl = "https://www.reddit.com/api/v1/authorize?state=RANDOM_STRING"
            every { buildAuthUrl.execute(CLIENT_ID, REDIRECT_URL) } returns authUrl

            //Act
            presenter.onLogInClicked()

            //Assert
            verify { view.showLoginPage(authUrl) }
        }

        @Test
        fun `hide login prompt`() {
            //Act
            presenter.onLogInClicked()

            //Assert
            verify { view.hideLoginPrompt() }
        }
    }

    @Nested
    inner class OnPageStarted {
        @Test
        fun `when url is final should hide login ui`() {
            //Act
            presenter.onPageStarted(FINAL_REDIRECT_URL)

            //Assert
            verify { view.hideLoginPage() }
        }

        @Test
        fun `when url is not the redirect url should keep the login ui`() {
            //Act
            presenter.onPageStarted("https://invalid.com")

            //Assert
            verify { view wasNot Called }
        }

        @Test
        fun `when url doesn't contain any query should keep the login ui`() {
            //Act
            presenter.onPageStarted(REDIRECT_URL)

            //Assert
            verify { view wasNot Called }
        }

        @Test
        fun `when url is final should authenticate user`() {
            //Arrange
            val authUrl = "https://www.reddit.com/api/v1/authorize?state="
            every { buildAuthUrl.execute(any(), any()) }.returns(authUrl + "FIRST_STATE")
                .andThen(authUrl + "SECOND_STATE")

            //Act
            presenter.onLogInClicked()
            presenter.onPageStarted(FINAL_REDIRECT_URL)

            //Assert
            verify {
                authenticateUser.execute(REDIRECT_URL, FINAL_REDIRECT_URL, authUrl + "FIRST_STATE")
            }
        }

        @Test
        fun `when login is successful should reload UI`() {
            // Arrange
            every { authenticateUser.execute(any(), any(), any()) } returns Completable.complete()

            //Act
            presenter.onPageStarted(FINAL_REDIRECT_URL)

            // Assert
            verify { view.reloadScreen() }
        }
    }
}