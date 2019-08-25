package com.visualeap.aliforreddit.data.repository

import com.visualeap.aliforreddit.data.network.token.TokenResponse
import com.visualeap.aliforreddit.data.repository.token.TokenDataRepository
import com.visualeap.aliforreddit.data.repository.token.TokenLocalSource
import com.visualeap.aliforreddit.data.repository.token.TokenRemoteSource
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
import util.domain.*
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
            val fetchedToken = createUserToken()
            val expectedToken = createUserToken(
                ID,
                fetchedToken.accessToken,
                fetchedToken.type,
                fetchedToken.refreshToken
            )
            every { remote.getUserToken(CODE) } returns Single.just(fetchedToken)
            every { local.addUserToken(any()) } returns Single.just(ID)
            every { local.getUserToken(ID) } returns Single.just(expectedToken)

            //Act, Assert
            tokenRepository.getUserToken(CODE)
                .test()
                .assertResult(expectedToken)
        }

        @Test
        fun `cache user token`() {
            //Arrange
            val userToken = createUserToken(id = NOT_SET_ROW_ID)
            every { remote.getUserToken(any()) } returns Single.just(userToken)

            //Act
            tokenRepository.getUserToken(CODE)
                .test()

            //Assert
            verify { local.addUserToken(userToken) }
        }

        @Test
        fun `return error when caching user token fails`() {
            //Arrange
            every { remote.getUserToken(any()) } returns Single.just(createUserToken())
            every { local.addUserToken(any()) } throws SQLException()

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
            val fetchedToken = createUserlessToken()
            val expectedToken =
                createUserlessToken(
                    ID,
                    fetchedToken.accessToken,
                    fetchedToken.type,
                    DEVICE_ID
                )
            every { remote.getUserlessToken(DEVICE_ID) } returns Single.just(fetchedToken)
            every { local.setUserlessToken(any()) } returns Completable.complete()
            every { local.getUserlessToken() } returns Single.just(expectedToken)

            //Act, Assert
            tokenRepository.getUserlessToken(DEVICE_ID)
                .test()
                .assertResult(expectedToken)
        }

        @Test
        fun `cache user-less token`() {
            //Arrange
            val fetchedToken = createUserlessToken(id = NOT_SET_ROW_ID)
            every { remote.getUserlessToken(any()) } returns Single.just(fetchedToken)

            //Act
            tokenRepository.getUserlessToken(DEVICE_ID)
                .test()

            //Assert
            verify { local.setUserlessToken(fetchedToken) }
        }

        @Test
        fun `return error when caching user-less token fails`() {
            //Arrange
            every { remote.getUserlessToken(any()) } returns Single.just(createUserlessToken())
            every { local.setUserlessToken(any()) } throws SQLException()

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
            val fetchedToken = createUserToken()
            val expectedToken =
                createUserToken(
                    ID,
                    fetchedToken.accessToken,
                    fetchedToken.type,
                    REFRESH_TOKEN
                )
            every { remote.refreshUserToken(REFRESH_TOKEN) } returns Single.just(fetchedToken)
            every { local.updateUserToken(any()) } returns Completable.complete()
            every { local.getUserToken(any()) } returns Single.just(expectedToken)

            //Act, Assert
            tokenRepository.refreshUserToken(ID, REFRESH_TOKEN)
                .test()
                .assertResult(expectedToken)
        }

        @Test
        fun `update cached user token`() {
            //Arrange
            val fetchedToken = createUserToken()
            every { remote.refreshUserToken(any()) } returns Single.just(fetchedToken)

            //Act
            val expectedToken = createUserToken(
                ID,
                fetchedToken.accessToken,
                fetchedToken.type,
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
            every { remote.refreshUserToken(any()) } returns Single.just(createUserToken())
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
            val userlessToken = createUserlessToken(id = randomInteger)
            every { remote.getUserlessToken(DEVICE_ID) } returns Single.just(createUserlessToken())
            every { local.setUserlessToken(any()) } returns Completable.complete()
            every { local.getUserlessToken() } returns Single.just(userlessToken)

            //Act, Assert
            tokenRepository.refreshUserlessToken(DEVICE_ID)
                .test()
                .assertResult(userlessToken)
        }

        @Test
        fun `update the cached user-less token`() {
            //Arrange
            val fetchedToken = createUserlessToken()
            every { remote.getUserlessToken(any()) } returns Single.just(fetchedToken)

            //Act
            tokenRepository.refreshUserlessToken(DEVICE_ID)
                .test()

            //Assert
            verify { local.setUserlessToken(fetchedToken) }
        }

        @Test
        fun `return error when updating cached user-less token fails`() {
            //Arrange
            every { remote.getUserlessToken(any()) } returns Single.just(createUserlessToken())
            every { local.setUserlessToken(any()) } throws SQLException()

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
}