package com.visualeap.aliforreddit.domain.authentication

import androidx.room.EmptyResultSetException
import com.visualeap.aliforreddit.data.token.AuthService
import com.visualeap.aliforreddit.data.token.TokenResponse
import com.visualeap.aliforreddit.domain.util.BasicAuthCredentialProvider
import com.visualeap.aliforreddit.domain.authentication.token.Token
import com.visualeap.aliforreddit.domain.authentication.token.UserlessToken
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
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
    private val authCredentialProvider: BasicAuthCredentialProvider = mockk(relaxed = true)
    private val getToken =
        FetchToken(
            authService,
            tokenRepository,
            authCredentialProvider
        )

    companion object {
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
        every { tokenRepository.getCurrentToken() } returns Single.just(expectedToken)

        //Act, Assert
        getToken.execute()
            .test()
            .assertValue(match { assertThat(it).isEqualTo(expectedToken) })
    }

    @Test
    fun `when current token doesn't exist should fetch userless token and save it`() {
        //Arrange
        every { tokenRepository.getCurrentToken() } returns Single.error(EmptyResultSetException(""))

        val basicAuth = "BASIC_AUTH_CREDENTIAL"
        every { authCredentialProvider.getAuthCredential() } returns basicAuth

        val deviceIdSlot = slot<String>()
        val fetchedToken = createTokenResponse()
        every { authService.getUserlessToken(GRANT_TYPE, capture(deviceIdSlot), basicAuth) }
            .returns(Single.just(fetchedToken))

        //Act
        getToken.execute()
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
        every { tokenRepository.getCurrentToken() } returns Single.error(EmptyResultSetException(""))

        val fetchedToken = createTokenResponse()
        every { authService.getUserlessToken(any(), any(), any()) }
            .returns(Single.just(fetchedToken))
        every { tokenRepository.setUserlessToken(any()) } returns Single.just(202)

        //Act
        getToken.execute()
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
        every { tokenRepository.getCurrentToken() } returns Single.error(EmptyResultSetException(""))
        val deviceIdSlot = slot<String>()
        val fetchedToken = createTokenResponse()
        every { authService.getUserlessToken(any(), capture(deviceIdSlot), any()) }
            .returns(Single.just(fetchedToken))
        every { tokenRepository.setUserlessToken(any()) } returns Single.just(202)

        //Act, Assert
        getToken.execute()
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