package com.visualeap.aliforreddit.data.cache.token

import com.visualeap.aliforreddit.domain.model.token.Token
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
import util.data.createTokenEntity
import util.data.createUserTokenEntity
import util.data.createUserlessTokenEntity
import util.domain.*
import java.sql.SQLException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class TokenLsTest {
    private val tokenDao: TokenDao = mockk()
    private val tokenLs = TokenLs(tokenDao)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Nested
    inner class AddUserToken {
        @Test
        fun `add user token`() {
            //Arrange
            val userToken = createUserToken(id = randomInteger)
            every {
                tokenDao.addUserToken(
                    createTokenEntity(id = NOT_SET_ROW_ID),
                    createUserTokenEntity(id = NOT_SET_ROW_ID)
                )
            } returns ID

            //Act, Assert
            tokenLs.addUserToken(userToken)
                .test()
                .assertResult(ID)
        }

        @Test
        fun `return error when adding user token fails`() {
            //Arrange
            every { tokenDao.addUserToken(any(), any()) } throws SQLException()

            //Act, Assert
            tokenLs.addUserToken(createUserToken())
                .test()
                .assertFailure(SQLException::class.java)
        }
    }

    @Test
    fun `return user token`() {
        //arrange
        val rowId = randomInteger
        every { tokenDao.getTokenEntity(rowId) } returns Single.just(createTokenEntity())
        every { tokenDao.getUserTokenEntity(rowId) } returns Single.just(createUserTokenEntity())

        //Act, assert
        tokenLs.getUserToken(rowId)
            .test()
            .assertResult(createUserToken())
    }

    @Nested
    inner class SetUserlessToken {
        @Test
        fun `add new user-less token when no token exists`() {
            //Arrange
            every { tokenDao.getAllUserlessTokenEntities() } returns Single.just(emptyList())
            every { tokenDao.addUserlessToken(any(), any()) } just runs

            //Act, Assert
            val userlessToken = createUserlessToken()
            tokenLs.setUserlessToken(userlessToken)
                .test()
                .assertResult()

            val expectedTokenEntity =
                createTokenEntity(NOT_SET_ROW_ID, userlessToken.accessToken, userlessToken.type)
            val expectedUserlessTokenEntity =
                createUserlessTokenEntity(NOT_SET_ROW_ID, userlessToken.deviceId)
            verify { tokenDao.addUserlessToken(expectedTokenEntity, expectedUserlessTokenEntity) }
        }

        @Test
        fun `update existing user-less token`() {
            //Arrange
            val rowId = randomInteger
            every { tokenDao.getAllUserlessTokenEntities() } returns Single.just(
                listOf(createUserlessTokenEntity(id = rowId))
            )
            every { tokenDao.updateUserlessToken(any(), any()) } just runs

            //Act, Assert
            val userlessToken = createUserlessToken()
            tokenLs.setUserlessToken(userlessToken)
                .test()
                .assertResult()

            val expectedTokenEntity =
                createTokenEntity(rowId, userlessToken.accessToken, userlessToken.type)
            val expectedUserlessTokenEntity =
                createUserlessTokenEntity(rowId, userlessToken.deviceId)
            verify {
                tokenDao.updateUserlessToken(expectedTokenEntity, expectedUserlessTokenEntity)
            }
            verify(exactly = 0) { tokenDao.addUserlessToken(any(), any()) }
        }

        @Test
        fun `return error when more than a single user-less token exists`() {
            //Arrange
            every { tokenDao.getAllUserlessTokenEntities() } returns Single.just(
                listOf(createUserlessTokenEntity(), createUserlessTokenEntity())
            )

            //Act, Assert
            tokenLs.setUserlessToken(createUserlessToken())
                .test()
                .assertFailure(IllegalArgumentException::class.java)
        }
    }

    @Test
    fun `return user-less token`() {
        //Arrange
        val rowId = randomInteger
        val userlessTokenEntity = createUserlessTokenEntity(id = rowId)
        val tokenEntity = createTokenEntity(id = rowId)
        every { tokenDao.getAllUserlessTokenEntities() } returns Single.just(
            listOf(userlessTokenEntity)
        )
        every { tokenDao.getTokenEntity(rowId) } returns Single.just(tokenEntity)

        //Act, Assert
        val userlessToken = createUserlessToken(
            tokenEntity.id,
            tokenEntity.accessToken,
            tokenEntity.type,
            userlessTokenEntity.deviceId
        )
        tokenLs.getUserlessToken()
            .test()
            .assertResult(userlessToken)
    }

    @Test
    fun `update user token`() {
        //Arrange
        every { tokenDao.updateUserToken(any(), any()) } just runs
        //Act, Assert
        val userToken = createUserToken()
        tokenLs.updateUserToken(userToken)
            .test()
            .assertResult()

        val tokenEntity = TokenEntity(userToken.id, userToken.accessToken, userToken.type)
        val userTokenEntity = UserTokenEntity(userToken.id, userToken.refreshToken)
        verify { tokenDao.updateUserToken(tokenEntity, userTokenEntity) }
    }

    @Nested
    inner class GetCurrentToken {
        @Test
        fun `return token when current token is a user token`() {
            //Arrange
            val rowId = randomInteger
            val tokenEntity = createTokenEntity(id = rowId)
            val userTokenEntity = createUserTokenEntity(id = rowId)
            every { tokenDao.getCurrentTokenEntity() } returns Maybe.just(
                tokenEntity
            )
            every { tokenDao.getUserTokenEntity(rowId) } returns Single.just(userTokenEntity)

            //Act, Assert
            val expectedToken: Token = createUserToken(
                tokenEntity.id,
                tokenEntity.accessToken,
                tokenEntity.type,
                userTokenEntity.refreshToken
            )
            tokenLs.getCurrentToken()
                .test()
                .assertResult(expectedToken)
        }

        @Test
        fun `return token when current token is a user-less token`() {
            //Arrange
            val rowId = randomInteger
            val tokenEntity = createTokenEntity(id = rowId)
            val userlessTokenEntity = createUserlessTokenEntity(id = rowId)
            every { tokenDao.getCurrentTokenEntity() } returns Maybe.just(
                tokenEntity
            )
            every { tokenDao.getUserlessTokenEntity(rowId) } returns Single.just(userlessTokenEntity)
            every { tokenDao.getUserTokenEntity(any()) } returns Single.error(Throwable())

            //Act, Assert
            val expectedToken: Token = createUserlessToken(
                tokenEntity.id,
                tokenEntity.accessToken,
                tokenEntity.type,
                userlessTokenEntity.deviceId
            )
            tokenLs.getCurrentToken()
                .test()
                .assertResult(expectedToken)
        }

        @Test
        fun `complete when there's no current token`() {
            //Arrange
            every { tokenDao.getCurrentTokenEntity() } returns Maybe.empty()

            //Act, Assert
            tokenLs.getCurrentToken()
                .test()
                .assertComplete()
        }
    }

    @Test
    fun `set current token`() {
        //Arrange
        every { tokenDao.addCurrentTokenEntity(any()) } returns Completable.complete()

        //Act, Assert
        val token = createToken(id = randomInteger)
        tokenLs.setCurrentToken(token)
            .test()
            .assertResult()

        val currentTokenEntity = CurrentTokenEntity(SINGLE_RECORD_ID, token.id)
        verify { tokenDao.addCurrentTokenEntity(currentTokenEntity) }
    }
}