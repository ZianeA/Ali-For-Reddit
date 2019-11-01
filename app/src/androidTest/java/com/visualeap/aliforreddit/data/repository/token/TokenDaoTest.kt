package com.visualeap.aliforreddit.data.repository.token

import androidx.test.runner.AndroidJUnit4
import org.junit.runner.RunWith
import org.junit.After
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import com.visualeap.aliforreddit.data.cache.RedditDatabase
import org.junit.Before
import org.junit.Test
import util.domain.*

@RunWith(AndroidJUnit4::class)
internal class TokenDaoTest {
    private lateinit var tokenDao: TokenDao
    private lateinit var db: RedditDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getContext()
        db = Room.inMemoryDatabaseBuilder(context, RedditDatabase::class.java)
            .build()
        tokenDao = db.tokenDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetUserToken() {
        //Arrange
        val tokenEntity = createTokenEntity(id = randomInteger)
        val userTokenEntity = createUserTokenEntity(id = randomInteger)

        //Act
        tokenDao.addUserToken(TokenWithUserTokenEntity(tokenEntity, userTokenEntity))

        //Assert
        val expectedToken = TokenWithUserTokenEntity(
            tokenEntity.copy(id = SINGLE_RECORD_ID),
            userTokenEntity.copy(id = SINGLE_RECORD_ID)
        )
        tokenDao.getTokenWithUserTokenEntity(SINGLE_RECORD_ID)
            .test()
            .assertResult(expectedToken)
    }

    @Test
    fun setAndGetUserlessToken() {
        //Arrange
        val tokenEntity = createTokenEntity(id = randomInteger)
        val userlessTokenEntity = createUserlessTokenEntity(id = randomInteger)

        //Act
        tokenDao.setUserlessToken(TokenWithUserlessTokenEntity(tokenEntity, userlessTokenEntity))
        val actualToken = tokenDao.getTokenWithUserlessTokenEntity()

        //Assert
        val expectedToken = TokenWithUserlessTokenEntity(
            tokenEntity.copy(id = SINGLE_RECORD_ID),
            userlessTokenEntity.copy(id = SINGLE_RECORD_ID)
        )
        assert(actualToken == expectedToken)
    }

    @Test
    fun thereShouldBeOnlyOneUserlessToken() {
        //Arrange
        val token1 = TokenWithUserlessTokenEntity(
            createTokenEntity(id = randomInteger, accessToken = "First Access Token"),
            createUserlessTokenEntity(id = randomInteger)
        )
        val token2 = TokenWithUserlessTokenEntity(
            createTokenEntity(id = randomInteger, accessToken = "Second Access Token"),
            createUserlessTokenEntity(id = randomInteger)
        )

        //Act
        tokenDao.setUserlessToken(token1)
        tokenDao.setUserlessToken(token2)
        val token = tokenDao.getTokenWithUserlessTokenEntity()

        //Assert
        val expectedToken =
            token2.copy(tokenEntity = token2.tokenEntity.copy(id = SINGLE_RECORD_ID))
        assert(token == expectedToken)
    }

    @Test
    fun updateUserToken() {
        //Arrange
        tokenDao.addUserToken(
            TokenWithUserTokenEntity(
                createTokenEntity(),
                createUserTokenEntity()
            )
        )

        //Act
        val updatedTokenEntity =
            createTokenEntity(id = SINGLE_RECORD_ID, accessToken = "Updated Access Token")
        val updatedUserTokenEntity =
            createUserTokenEntity(id = SINGLE_RECORD_ID, refreshToken = "Updated Refresh Token")
        tokenDao.updateUserToken(updatedTokenEntity, updatedUserTokenEntity)
            .test()
            .assertResult()

        //Assert
        tokenDao.getTokenWithUserTokenEntity(SINGLE_RECORD_ID)
            .test()
            .assertResult(TokenWithUserTokenEntity(updatedTokenEntity, updatedUserTokenEntity))
    }

    @Test
    fun setAndGetCurrentToken() {
        //Arrange
        val tokenId = tokenDao.addUserToken(createTokenWithUserTokenEntity())
        val currentTokenEntity =
            CurrentTokenEntity(
                SINGLE_RECORD_ID,
                tokenId,
                CurrentTokenEntity.TokenType.USER
            )

        //Act, Assert
        tokenDao.setCurrentTokenEntity(currentTokenEntity)
            .test()
            .assertResult()

        tokenDao.getCurrentTokenEntity()
            .test()
            .assertResult(currentTokenEntity)
    }
}