package com.visualeap.aliforreddit.data.repository

import com.visualeap.aliforreddit.data.repository.token.CurrentTokenEntity.*
import com.visualeap.aliforreddit.data.repository.token.TokenDao
import com.visualeap.aliforreddit.data.network.auth.AuthService
import com.visualeap.aliforreddit.data.repository.token.TokenResponse
import com.visualeap.aliforreddit.data.repository.token.*
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
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

    private val remote: AuthService = mockk()
    private val local: TokenDao = mockk()
    private val basicAuth = createBasicAuth()
    private val userTokenMapper: Mapper<TokenWithUserTokenEntity, UserToken> = mockk()
    private val userlessTokenMapper: Mapper<TokenWithUserlessTokenEntity, UserlessToken> = mockk()
    private val tokenRepository: TokenDataRepository =
        TokenDataRepository(
            remote,
            local,
            userTokenMapper,
            userlessTokenMapper,
            REDIRECT_URL,
            basicAuth
        )

    companion object {
        private const val USER_TOKEN_GRANT_TYPE = "authorization_code"
        private const val REFRESH_TOKEN_GRANT_TYPE = "refresh_token"
        private const val USERLESS_TOKEN_GRANT_TYPE =
            "https://oauth.reddit.com/grants/installed_client"
    }

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
            val userToken = createUserToken(id = NOT_SET_ROW_ID)
            val tokenWithUserTokenEntity = createTokenWithUserTokenEntity()
            every {
                remote.getUserToken(
                    USER_TOKEN_GRANT_TYPE,
                    CODE,
                    REDIRECT_URL,
                    basicAuth
                )
            } returns Single.just(tokenResponse)
            every { userTokenMapper.mapReverse(userToken) } returns tokenWithUserTokenEntity
            every { local.addUserToken(tokenWithUserTokenEntity) } returns ID
            every { local.getTokenWithUserTokenEntity(ID) } returns Single.just(
                tokenWithUserTokenEntity
            )
            every { userTokenMapper.map(tokenWithUserTokenEntity) } returns userToken

            //Act, Assert
            tokenRepository.getUserToken(CODE)
                .test()
                .assertResult(userToken)
        }

        @Test
        fun `cache user token`() {
            //Arrange
            val tokenWithUserTokenEntity = createTokenWithUserTokenEntity()
            every { remote.getUserToken(any(), any(), any(), any()) } returns Single.just(
                createTokenResponse()
            )
            every { userTokenMapper.mapReverse(any()) } returns tokenWithUserTokenEntity
            every { local.addUserToken(any()) } returns ID

            //Act
            tokenRepository.getUserToken(CODE)
                .test()

            //Assert
            verify { local.addUserToken(tokenWithUserTokenEntity) }
        }

        @Test
        fun `return error when caching user token fails`() {
            //Arrange
            every { remote.getUserToken(any(), any(), any(), any()) } returns Single.just(
                createTokenResponse()
            )
            every { userTokenMapper.mapReverse(any()) } returns createTokenWithUserTokenEntity()
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
            val tokenWithUserlessToken = createTokenWithUserlessTokenEntity()
            val tokenResponse = createTokenResponse(refreshToken = null)
            val userlessToken = createUserlessToken(id = NOT_SET_ROW_ID)
            every {
                remote.getUserlessToken(
                    USERLESS_TOKEN_GRANT_TYPE,
                    DEVICE_ID,
                    basicAuth
                )
            } returns Single.just(tokenResponse)
            every { userlessTokenMapper.mapReverse(userlessToken) } returns tokenWithUserlessToken
            every { local.setUserlessToken(tokenWithUserlessToken) } just runs
            every { local.getTokenWithUserlessTokenEntity() } returns tokenWithUserlessToken
            every { userlessTokenMapper.map(tokenWithUserlessToken) } returns userlessToken

            //Act, Assert
            tokenRepository.getUserlessToken(DEVICE_ID)
                .test()
                .assertResult(userlessToken)
        }

        @Test
        fun `cache user-less token`() {
            //Arrange
            val tokenWithUserlessToken = createTokenWithUserlessTokenEntity()
            every { remote.getUserlessToken(any(), any(), any()) } returns Single.just(
                createTokenResponse(refreshToken = null)
            )
            every { userlessTokenMapper.mapReverse(any()) } returns tokenWithUserlessToken
            every { local.setUserlessToken(any()) } just runs

            //Act
            tokenRepository.getUserlessToken(DEVICE_ID)
                .test()

            //Assert
            verify { local.setUserlessToken(tokenWithUserlessToken) }
        }

        @Test
        fun `return error when caching user-less token fails`() {
            //Arrange
            every { remote.getUserlessToken(any(), any(), any()) } returns Single.just(
                createTokenResponse(refreshToken = null)
            )
            every { userlessTokenMapper.mapReverse(any()) } returns createTokenWithUserlessTokenEntity()
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
            val tokenResponse = createTokenResponse()
            val userToken = createUserToken(id = NOT_SET_ROW_ID)
            val tokenWithUserTokenEntity = createTokenWithUserTokenEntity()
            every {
                remote.refreshUserToken(REFRESH_TOKEN_GRANT_TYPE, REFRESH_TOKEN)
            } returns Single.just(tokenResponse)
            every { userTokenMapper.mapReverse(userToken) } returns tokenWithUserTokenEntity
            every {
                local.updateUserToken(
                    tokenWithUserTokenEntity.tokenEntity,
                    tokenWithUserTokenEntity.userTokenEntity
                )
            } returns Completable.complete()
            every {
                local.getTokenWithUserTokenEntity(tokenWithUserTokenEntity.tokenEntity.id)
            } returns Single.just(tokenWithUserTokenEntity)
            every { userTokenMapper.map(tokenWithUserTokenEntity) } returns userToken

            //Act, Assert
            tokenRepository.refreshUserToken(ID, REFRESH_TOKEN)
                .test()
                .assertResult(userToken)
        }

        @Test
        fun `update cached user token`() {
            //Arrange
            val tokenWithUserTokenEntity = createTokenWithUserTokenEntity()
            every { remote.refreshUserToken(any(), any()) } returns Single.just(
                createTokenResponse()
            )
            every { userTokenMapper.mapReverse(any()) } returns tokenWithUserTokenEntity
            every { local.updateUserToken(any(), any()) } returns Completable.complete()

            //Act
            tokenRepository.refreshUserToken(ID, REFRESH_TOKEN)
                .test()

            //Assert
            verify {
                local.updateUserToken(
                    tokenWithUserTokenEntity.tokenEntity,
                    tokenWithUserTokenEntity.userTokenEntity
                )
            }
        }

        @Test
        fun `return error when updating cached user token fails`() {
            //Arrange
            every { remote.refreshUserToken(any(), any()) } returns Single.just(
                createTokenResponse()
            )
            every { userTokenMapper.mapReverse(any()) } returns createTokenWithUserTokenEntity()
            every { local.updateUserToken(any(), any()) } throws SQLException()

            //Act, Assert
            tokenRepository.refreshUserToken(ID, REFRESH_TOKEN)
                .test()
                .assertFailure(SQLException::class.java)
        }
    }

    @Nested
    inner class GetCurrentToken {
        @Test
        fun `return current token when it's a user token`() {
            //Arrange
            val currentTokenEntity = createCurrentTokenEntity(tokenType = TokenType.USER)
            val tokenWithUserTokenEntity = createTokenWithUserTokenEntity()
            val userToken = createUserToken()
            every { local.getCurrentTokenEntity() } returns Maybe.just(currentTokenEntity)
            every { local.getTokenWithUserTokenEntity(currentTokenEntity.tokenId) } returns Single.just(
                tokenWithUserTokenEntity
            )
            every { userTokenMapper.map(tokenWithUserTokenEntity) } returns userToken

            //Act, Assert
            tokenRepository.getCurrentToken()
                .test()
                .assertResult(userToken)
        }

        @Test
        fun `return current token when it's a userless token`() {
            val currentTokenEntity = createCurrentTokenEntity(tokenType = TokenType.USERLESS)
            val tokenWithUserlessTokenEntity = createTokenWithUserlessTokenEntity()
            val userlessToken = createUserlessToken()
            every { local.getCurrentTokenEntity() } returns Maybe.just(currentTokenEntity)
            every { local.getTokenWithUserlessTokenEntity() } returns tokenWithUserlessTokenEntity
            every { userlessTokenMapper.map(tokenWithUserlessTokenEntity) } returns userlessToken

            //Act, Assert
            tokenRepository.getCurrentToken()
                .test()
                .assertResult(userlessToken)
        }

        @Test
        fun `complete when there's no current token`() {
            every { local.getCurrentTokenEntity() } returns Maybe.empty()

            //Act, Assert
            tokenRepository.getCurrentToken()
                .test()
                .assertResult()
        }
    }

    @Nested
    inner class SetCurrentToken {
        @Test
        fun `set current token`() {
            //Arrange
            every { local.setCurrentTokenEntity(any()) } returns Completable.complete()
            //Act
            tokenRepository.setCurrentToken(createUserToken())
                .test()
                .assertResult()

            //Assert
            verify { local.setCurrentTokenEntity(createCurrentTokenEntity()) }
        }
    }
}