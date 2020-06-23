package com.visualeap.aliforreddit.data.token

import com.visualeap.aliforreddit.data.database.RedditDatabase
import com.visualeap.aliforreddit.data.token.CurrentTokenEntity.*
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Maybe
import io.reactivex.Single
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createCurrentTokenEntity
import util.domain.createUserToken
import util.domain.createUserlessToken
import util.domain.match
import java.util.concurrent.Callable

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TokenRoomRepositoryTest {
    private val db: RedditDatabase = mockk()
    private val tokenDao: TokenDao = mockk(relaxed = true)
    private val userTokenDao: UserTokenDao = mockk(relaxed = true)
    private val userlessTokenDao: UserlessTokenDao = mockk(relaxed = true)
    private val currentTokenDao: CurrentTokenDao = mockk(relaxed = true)
    private val repository =
        TokenRoomRepository(db, tokenDao, userTokenDao, userlessTokenDao, currentTokenDao)

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        every { db.runInTransaction(any()) } answers { firstArg<Runnable>().run() }
        every { db.runInTransaction<Int>(any()) } answers { firstArg<Callable<Int>>().call() }
    }

    @Nested
    inner class AddUserToken {
        @Test
        fun `should add token to database`() {
            //Arrange
            val token = createUserToken()
            every { tokenDao.addTokenEntity(any()) } returns 202

            //Act
            repository.addUserToken(token)
                .test()
                .assertNoErrors()

            //Assert
            verify {
                tokenDao.addTokenEntity(withArg {
                    assertThat(it).isEqualTo(TokenEntity(0, token.accessToken, token.type))
                })
            }
            verify {
                userTokenDao.addUserTokenEntity(withArg {
                    assertThat(it).isEqualTo(UserTokenEntity(202, token.refreshToken))
                })
            }
        }

        @Test
        fun `should return inserted token id`() {
            //Arrange
            val token = createUserToken()
            every { tokenDao.addTokenEntity(any()) } returns 202

            //Act and assert
            repository.addUserToken(token)
                .test()
                .assertResult(202)
        }
    }

    @Nested
    inner class UpdateUserToken {
        @Test
        fun `should update existing token`() {
            //Arrange
            val token = createUserToken()

            //Act
            repository.updateUserToken(token)
                .test()
                .assertResult()

            //Assert
            verify {
                tokenDao.updateTokenEntity(withArg {
                    assertThat(it).isEqualTo(TokenEntity(token.id, token.accessToken, token.type))
                })
            }
            verify {
                userTokenDao.updateUserTokenEntity(withArg {
                    assertThat(it).isEqualTo(UserTokenEntity(token.id, token.refreshToken))
                })
            }
        }
    }

    @Nested
    inner class SetUserlessToken {
        @Test
        fun `should add new token to database`() {
            //Arrange
            every { userlessTokenDao.getUserlessTokenId() } returns Maybe.empty()
            every { tokenDao.addTokenEntity(any()) } returns 202
            val token = createUserlessToken(id = 0)

            //Act
            repository.setUserlessToken(token)
                .test()
                .assertNoErrors()

            //Assert
            verify {
                tokenDao.addTokenEntity(withArg {
                    assertThat(it).isEqualTo(TokenEntity(0, token.accessToken, token.type))
                })
            }
            verify {
                userlessTokenDao.addUserlessTokenEntity(withArg {
                    assertThat(it).isEqualTo(UserlessTokenEntity(202, token.deviceId))
                })
            }
        }

        @Test
        fun `should return the id of newly inserted token`() {
            //Arrange
            every { userlessTokenDao.getUserlessTokenId() } returns Maybe.empty()
            every { tokenDao.addTokenEntity(any()) } returns 202

            //Act, assert
            repository.setUserlessToken(createUserlessToken(id = 0))
                .test()
                .assertResult(202)
        }

        @Test
        fun `when token exists should update it`() {
            //Arrange
            val token = createUserlessToken()
            every { userlessTokenDao.getUserlessTokenId() } returns Maybe.just(202)

            //Act
            repository.setUserlessToken(token)
                .test()
                .assertNoErrors()

            //Assert
            verify {
                tokenDao.updateTokenEntity(withArg {
                    assertThat(it).isEqualTo(TokenEntity(202, token.accessToken, token.type))
                })
            }
            verify {
                userlessTokenDao.updateUserlessTokenEntity(withArg {
                    assertThat(it).isEqualTo(UserlessTokenEntity(202, token.deviceId))
                })
            }
        }

        @Test
        fun `should return the id of updated token`() {
            //Arrange
            every { userlessTokenDao.getUserlessTokenId() } returns Maybe.just(202)

            //Act and assert
            repository.setUserlessToken(createUserlessToken())
                .test()
                .assertResult(202)
        }
    }

    @Nested
    inner class GetCurrentToken {
        @Test
        fun `when current token is user token should return it`() {
            //Arrange
            val currentTokenEntity = createCurrentTokenEntity(tokenType = TokenType.USER)
            val userToken = createUserToken()

            every { currentTokenDao.getCurrentTokenEntity() } returns Maybe.just(currentTokenEntity)
            every { userTokenDao.getUserToken(currentTokenEntity.tokenId) } returns Single.just(
                userToken
            )

            //Act and assert
            repository.getCurrentToken()
                .test()
                .assertValue(match { assertThat(it).isEqualTo(userToken) })
        }

        @Test
        fun `when current token is userless token should return it`() {
            val currentTokenEntity = createCurrentTokenEntity(tokenType = TokenType.USERLESS)
            val userlessToken = createUserlessToken()
            every { currentTokenDao.getCurrentTokenEntity() } returns Maybe.just(currentTokenEntity)
            every { userlessTokenDao.getUserlessToken() } returns Single.just(userlessToken)

            //Act, Assert
            repository.getCurrentToken()
                .test()
                .assertValue(match { assertThat(it).isEqualTo(userlessToken) })
        }

        @Test
        fun `when there's no current token should return empty`() {
            every { currentTokenDao.getCurrentTokenEntity() } returns Maybe.empty()

            //Act and assert
            repository.getCurrentToken()
                .test()
                .assertResult()
        }
    }

    @Nested
    inner class SetCurrentToken {
        @Test
        fun `should save user token as current token`() {
            //Arrange
            val token = createUserToken()

            //Act
            repository.setCurrentToken(token)
                .test()
                .assertResult()

            //Assert
            verify {
                currentTokenDao.setCurrentTokenEntity(
                    withArg {
                        assertThat(it)
                            .isEqualTo(CurrentTokenEntity(1, token.id, TokenType.USER))
                    }
                )
            }
        }

        @Test
        fun `should save userless token as current token`() {
            //Arrange
            val token = createUserlessToken()

            //Act
            repository.setCurrentToken(token)
                .test()
                .assertResult()

            //Assert
            verify {
                currentTokenDao.setCurrentTokenEntity(
                    withArg {
                        assertThat(it)
                            .isEqualTo(CurrentTokenEntity(1, token.id, TokenType.USERLESS))
                    }
                )
            }
        }
    }
}