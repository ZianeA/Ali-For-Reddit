package com.visualeap.aliforreddit.data.cache.token

import androidx.test.runner.AndroidJUnit4
import org.junit.runner.RunWith
import org.junit.After
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import com.visualeap.aliforreddit.data.cache.RedditDatabase
import org.junit.Before
import org.junit.Test
import util.data.createTokenEntity
import util.domain.createUserToken
import util.domain.createUserlessToken
import util.data.createUserTokenEntity
import util.data.createUserlessTokenEntity
import util.domain.NOT_SET_ROW_ID
import util.domain.SINGLE_RECORD_ID
import java.lang.UnsupportedOperationException

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
        val tokenEntity = createTokenEntity(id = NOT_SET_ROW_ID)
        val userTokenEntity = createUserTokenEntity(id = NOT_SET_ROW_ID)

        //Act
        val rowId = tokenDao.addUserToken(tokenEntity, userTokenEntity)

        //Assert
        val expectedUserTokenEntity = userTokenEntity.copy(id = rowId)
        val expectedTokenEntity = tokenEntity.copy(id = rowId)
        tokenDao.getUserTokenEntity(rowId)
            .test()
            .assertResult(expectedUserTokenEntity)

        tokenDao.getTokenEntity(rowId)
            .test()
            .assertResult(expectedTokenEntity)
    }

    @Test
    fun insertAndGetUserlessToken() {
        //Arrange
        val tokenEntity = createTokenEntity(id = NOT_SET_ROW_ID)
        val userlessTokenEntity = createUserlessTokenEntity(id = NOT_SET_ROW_ID)

        //Act
        tokenDao.addUserlessToken(tokenEntity, userlessTokenEntity)

        //Assert
        tokenDao.getUserlessTokenEntity(id = 1)
            .test()
            .assertResult(userlessTokenEntity.copy(id = 1))

        tokenDao.getTokenEntity(id = 1)
            .test()
            .assertResult(tokenEntity.copy(id = 1))
    }

    @Test
    fun updateUserToken() {
        //Arrange
        val tokenId = tokenDao.addUserToken(
            createTokenEntity(id = NOT_SET_ROW_ID),
            createUserTokenEntity(id = NOT_SET_ROW_ID)
        )

        //Act
        val updatedTokenEntity =
            createTokenEntity(id = tokenId, accessToken = "Updated Access Token")
        val updatedUserTokenEntity =
            createUserTokenEntity(id = tokenId, refreshToken = "Updated Refresh Token")
        tokenDao.updateUserToken(updatedTokenEntity, updatedUserTokenEntity)

        //Assert
        tokenDao.getUserTokenEntity(tokenId)
            .test()
            .assertResult(updatedUserTokenEntity)

        tokenDao.getTokenEntity(tokenId)
            .test()
            .assertResult(updatedTokenEntity)
    }

    @Test
    fun updateUserlessToken() {
        //Arrange

        //Act
        tokenDao.addUserlessToken(
            createTokenEntity(id = NOT_SET_ROW_ID),
            createUserlessTokenEntity(id = NOT_SET_ROW_ID)
        )
        val updatedTokenEntity = createTokenEntity(id = 1, accessToken = "Updated Access Token")
        val updatedUserlessTokenEntity =
            createUserlessTokenEntity(id = 1, deviceId = "Updated Device ID")
        tokenDao.updateUserlessToken(updatedTokenEntity, updatedUserlessTokenEntity)

        //Assert
        tokenDao.getUserlessTokenEntity(1)
            .test()
            .assertResult(updatedUserlessTokenEntity)

        tokenDao.getTokenEntity(1)
            .test()
            .assertResult(updatedTokenEntity)
    }

    @Test
    fun setAndGetCurrentToken() {
        //Arrange
        val rowId = tokenDao.addUserToken(createTokenEntity(), createUserTokenEntity())
        tokenDao.addCurrentTokenEntity(CurrentTokenEntity(SINGLE_RECORD_ID, rowId))
            .test()
            .assertResult()

        //Act, Assert
        val expectedToken = createTokenEntity(id = rowId)
        tokenDao.getCurrentTokenEntity()
            .test()
            .assertResult(expectedToken)
    }
}