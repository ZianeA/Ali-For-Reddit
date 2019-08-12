package com.visualeap.aliforreddit.data.repository

import com.visualeap.aliforreddit.data.network.token.TokenResponse
import com.visualeap.aliforreddit.data.repository.token.TokenDataRepository
import com.visualeap.aliforreddit.data.repository.token.TokenLocalSource
import com.visualeap.aliforreddit.data.repository.token.TokenRemoteSource
import util.*
import io.mockk.*
import io.mockk.junit5.MockKExtension
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import java.sql.SQLException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class TokenDataRepositoryTest {

    private val remote: TokenRemoteSource = mockk()
    private val local: TokenLocalSource = mockk()
    private val tokenRepository: TokenDataRepository = TokenDataRepository(remote, local)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Nested
    inner class GetUserToken {
        @Test
        fun `return user token`() {
            //Arrange
            val tokenResponse = createTokenResponse()
            val expectedToken = createUserToken(
                ID,
                tokenResponse.accessToken,
                tokenResponse.type,
                tokenResponse.refreshToken!!
            )
            every { remote.getUserToken(CODE) } returns Single.just(tokenResponse)
            every { local.saveUserToken(any()) } returns ID
            every { local.getUserToken(ID) } returns Single.just(expectedToken)

            //Act, Assert
            tokenRepository.getUserToken(CODE)
                .test()
                .assertResult(expectedToken)
        }

        @Test
        fun `cache user token`() {
            //Arrange
            every { remote.getUserToken(any()) } returns Single.just(
                createTokenResponse()
            )

            //Act
            tokenRepository.getUserToken(CODE)
                .test()

            //Assert
            val expectedToken = createUserToken(id = NOT_SET_ROW_ID, refreshToken = REFRESH_TOKEN)
            verify { local.saveUserToken(expectedToken) }
        }

        @Test
        fun `return error when caching user token fails`() {
            //Arrange
            every { remote.getUserToken(any()) } returns Single.just(createTokenResponse())
            every { local.saveUserToken(any()) } throws SQLException()

            //Act, Assert
            tokenRepository.getUserToken(CODE)
                .test()
                .assertFailure(SQLException::class.java)
        }
    }

    @Nested
    inner class GetUserlessToken {
        @Test
        fun `return user-less token`() {
            //Arrange
            val tokenResponse = createTokenResponse()
            val expectedToken =
                createUserlessToken(
                    ID,
                    tokenResponse.accessToken,
                    tokenResponse.type,
                    DEVICE_ID
                )
            every { remote.getUserlessToken(DEVICE_ID) } returns Single.just(tokenResponse)
            every { local.saveUserlessToken(any()) } returns ID
            every { local.getUserlessToken() } returns Single.just(expectedToken)

            //Act, Assert
            tokenRepository.getUserlessToken(DEVICE_ID)
                .test()
                .assertResult(expectedToken)
        }

        @Test
        fun `cache user-less token`() {
            //Arrange
            val tokenResponse = createTokenResponse()
            every { remote.getUserlessToken(any()) } returns Single.just(tokenResponse)

            //Act
            tokenRepository.getUserlessToken(DEVICE_ID)
                .test()

            //Assert
            val expectedToken =
                createUserlessToken(
                    NOT_SET_ROW_ID,
                    tokenResponse.accessToken,
                    tokenResponse.type,
                    DEVICE_ID
                )
            verify { local.saveUserlessToken(expectedToken) }
        }

        @Test
        fun `return error when caching user-less token fails`() {
            //Arrange
            every { remote.getUserlessToken(any()) } returns Single.just(createTokenResponse())
            every { local.saveUserlessToken(any()) } throws SQLException()

            //Act, Assert
            tokenRepository.getUserlessToken(DEVICE_ID)
                .test()
                .assertFailure(SQLException::class.java)
        }
    }

    @Nested
    inner class RefreshUserToken {
        @Test
        fun `return refreshed user token`() {
            //Arrange
            val tokenResponse = createTokenResponse(refreshToken = null)
            val expectedToken =
                createUserToken(
                    ID,
                    tokenResponse.accessToken,
                    tokenResponse.type,
                    REFRESH_TOKEN
                )
            every { remote.refreshUserToken(REFRESH_TOKEN) } returns Single.just(tokenResponse)
            every { local.updateUserToken(any()) } just runs
            every { local.getUserToken(any()) } returns Single.just(expectedToken)

            //Act, Assert
            tokenRepository.refreshUserToken(ID, REFRESH_TOKEN)
                .test()
                .assertResult(expectedToken)
        }

        @Test
        fun `update cached user token`() {
            //Arrange
            val tokenResponse = createTokenResponse(refreshToken = null)
            every { remote.refreshUserToken(any()) } returns Single.just(tokenResponse)

            //Act
            val expectedToken = createUserToken(
                ID,
                tokenResponse.accessToken,
                tokenResponse.type,
                REFRESH_TOKEN
            )
            tokenRepository.refreshUserToken(ID, REFRESH_TOKEN)
                .test()

            //Assert
            verify { local.updateUserToken(expectedToken) }
        }

        @Test
        fun `return error when updating cached user token fails`() {
            //Arrange
            every { remote.refreshUserToken(any()) } returns Single.just(
                createTokenResponse(
                    refreshToken = null
                )
            )
            every { local.updateUserToken(any()) } throws SQLException()

            //Act, Assert
            tokenRepository.refreshUserToken(101, REFRESH_TOKEN)
                .test()
                .assertFailure(SQLException::class.java)
        }
    }

    @Nested
    inner class RefreshUserlessToken {
        @Test
        fun `return refreshed user-less token`() {
            //Arrange
            val tokenResponse = createTokenResponse()
            val expectedToken =
                createUserlessToken(
                    1,  //There's only one user-less token per app
                    tokenResponse.accessToken,
                    tokenResponse.type,
                    DEVICE_ID
                )
            every { remote.getUserlessToken(DEVICE_ID) } returns Single.just(tokenResponse)
            every { local.updateUserlessToken(any()) } just runs
            every { local.getUserlessToken() } returns Single.just(expectedToken)

            //Act, Assert
            tokenRepository.refreshUserlessToken(DEVICE_ID)
                .test()
                .assertResult(expectedToken)
        }

        @Test
        fun `update the cached user-less token`() {
            //Arrange
            val tokenResponse = createTokenResponse()
            every { remote.getUserlessToken(any()) } returns Single.just(tokenResponse)

            //Act
            tokenRepository.refreshUserlessToken(DEVICE_ID)
                .test()

            //Assert
            val expectedToken =
                createUserlessToken(
                    1,  //There's only one user-less token per app
                    tokenResponse.accessToken,
                    tokenResponse.type,
                    DEVICE_ID
                )
            verify { local.updateUserlessToken(expectedToken) }
        }

        @Test
        fun `return error when updating cached user-less token fails`() {
            //Arrange
            val tokenResponse = createTokenResponse()
            every { remote.getUserlessToken(any()) } returns Single.just(tokenResponse)
            every { local.updateUserlessToken(any()) } throws SQLException()

            //Act, Assert
            tokenRepository.refreshUserlessToken(DEVICE_ID)
                .test()
                .assertFailure(SQLException::class.java)
        }
    }

    @Nested
    inner class GetCurrentToken {
        @Test
        fun `return current token`() {
            //Arrange
            val expectedToken = createUserToken()
            every { local.getCurrentToken() } returns Maybe.just(expectedToken)

            //Act, Assert
            tokenRepository.getCurrentToken()
                .test()
                .assertResult(expectedToken)
        }
    }

    @Nested
    inner class SetCurrentToken {
        @Test
        fun `set current token`() {
            //Arrange
            every { local.setCurrentToken(any()) } returns Completable.complete()
            //Act
            val token = createUserToken()
            tokenRepository.setCurrentToken(token)
                .test()

            //Assert
            verify { local.setCurrentToken(token) }
        }
    }

    fun createTokenResponse(
        accessToken: String = ACCESS_TOKEN,
        type: String = TOKEN_TYPE,
        refreshToken: String? = REFRESH_TOKEN
    ) =
        TokenResponse(accessToken, type, refreshToken)
}