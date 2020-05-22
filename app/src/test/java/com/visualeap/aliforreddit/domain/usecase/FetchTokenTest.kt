package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.data.network.auth.AuthService
import com.visualeap.aliforreddit.data.repository.token.TokenResponse
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import okhttp3.Credentials
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createToken
import util.domain.createTokenResponse
import util.domain.match

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FetchTokenTest {
    private val authService: AuthService = mockk()
    private val tokenRepository: TokenRepository = mockk()
    private val getToken = FetchToken(authService, tokenRepository)

    companion object {
        private const val CLIENT_ID = "CLIENT_ID"
        private const val GRANT_TYPE =
            "https://oauth.reddit.com/grants/installed_client"
    }

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()

        // Set defaults
        every { authService.getUserlessToken(any(), any(), any()) } returns Single.just(
            TokenResponse("", "", null)
        )
        every { tokenRepository.setCurrentToken(any()) } returns Completable.complete()
        every { tokenRepository.setUserlessToken(any()) } returns Single.just(0)
    }

    @Test
    fun `when current token exists should return it`() {
        //Arrange
        val expectedToken = createToken()
        every { tokenRepository.getCurrentToken() } returns Maybe.just(expectedToken)

        //Act, Assert
        getToken.execute(CLIENT_ID)
            .test()
            .assertValue(match { assertThat(it).isEqualTo(expectedToken) })
    }

    @Test
    fun `when current token doesn't exist should fetch userless token and save it`() {
        //Arrange
        every { tokenRepository.getCurrentToken() } returns Maybe.empty()

        val basicAuth = Credentials.basic(CLIENT_ID, "")
        val deviceIdSlot = slot<String>()
        val fetchedToken = createTokenResponse()
        every { authService.getUserlessToken(GRANT_TYPE, capture(deviceIdSlot), basicAuth) }
            .returns(Single.just(fetchedToken))

        //Act
        getToken.execute(CLIENT_ID)
            .test()
            .assertNoErrors()

        // Assert
        verify {
            tokenRepository.setUserlessToken(withArg {
                assertThat(it).isEqualTo(
                    UserlessToken(
                        0,
                        fetchedToken.accessToken,
                        fetchedToken.type,
                        deviceIdSlot.captured
                    )
                )
            })
        }
    }

    @Test
    fun `when current token doesn't exist should set fetched token as current token`() {
        //Arrange
        every { tokenRepository.getCurrentToken() } returns Maybe.empty()

        val fetchedToken = createTokenResponse()
        every { authService.getUserlessToken(any(), any(), any()) }
            .returns(Single.just(fetchedToken))
        every { tokenRepository.setUserlessToken(any()) } returns Single.just(202)

        //Act
        getToken.execute(CLIENT_ID)
            .test()
            .assertNoErrors()

        // Assert
        verify {
            tokenRepository.setCurrentToken(withArg {
                assertThat(it).isInstanceOf(UserlessToken::class.java)
                    .extracting(Token::id).isEqualTo(202)
            })
        }
    }

    @Test
    fun `when current token doesn't exist should return userless token`() {
        //Arrange
        every { tokenRepository.getCurrentToken() } returns Maybe.empty()
        val deviceIdSlot = slot<String>()
        val fetchedToken = createTokenResponse()
        every { authService.getUserlessToken(any(), capture(deviceIdSlot), any()) }
            .returns(Single.just(fetchedToken))
        every { tokenRepository.setUserlessToken(any()) } returns Single.just(202)

        //Act, Assert
        getToken.execute(CLIENT_ID)
            .test()
            .assertValue(match {
                assertThat(it).isEqualTo(
                    UserlessToken(
                        202,
                        fetchedToken.accessToken,
                        fetchedToken.type,
                        deviceIdSlot.captured
                    )
                )
            })
    }
}