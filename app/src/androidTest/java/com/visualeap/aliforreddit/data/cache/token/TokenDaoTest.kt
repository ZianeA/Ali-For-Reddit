package com.visualeap.aliforreddit.data.cache.token

import androidx.test.runner.AndroidJUnit4
import org.junit.runner.RunWith
import org.junit.After
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import com.visualeap.aliforreddit.data.cache.RedditDatabase
import org.junit.Before
import org.junit.Test
import util.createUserToken
import util.createUserlessToken
import java.lang.UnsupportedOperationException
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
internal class TokenDaoTest {
    private lateinit var tokenDao: TokenDao
    private lateinit var db: RedditDatabase
    private val randomId = Random.nextInt()

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
        val userToken = createUserToken(id = randomId)

        //Act
        val rowId = tokenDao.saveUserToken(userToken)

        //Assert
        val expectedUserToken = userToken.copy(id = 1)
        tokenDao.getUserToken(rowId)
            .test()
            .assertResult(expectedUserToken)
    }

    @Test
    fun insertAndGetUserlessToken() {
        //Arrange
        val userlestoken = createUserlessToken(id = randomId)

        //Act
        tokenDao.saveUserlessToken(userlestoken)

        //Assert
        val expectedUserlessToken = userlestoken.copy(id = 1)
        tokenDao.getUserlessToken()
            .test()
            .assertResult(expectedUserlessToken)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun returnErrorWhenTryingToSaveMoreThanOneUserlessToken() {
        //Act
        tokenDao.saveUserlessToken(createUserlessToken(id = 101))
        tokenDao.saveUserlessToken(createUserlessToken(id = 102))
    }

    @Test
    fun updateUserToken() {
        //Arrange
        val userToken = createUserToken()

        //Act
        val tokenId = tokenDao.saveUserToken(userToken)
        val updatedUserToken = userToken.copy(
            id = tokenId,
            accessToken = "Updated Access Token",
            refreshToken = "Updated Refresh Token"
        )
        tokenDao.updateUserToken(updatedUserToken)

        //Assert
        tokenDao.getUserToken(tokenId)
            .test()
            .assertResult(updatedUserToken)
    }

    @Test
    fun updateUserlessToken() {
        //Arrange
        val userlessToken = createUserlessToken()

        //Act
        val tokenId = tokenDao.saveUserlessToken(userlessToken)
        val updatedUserlessToken = userlessToken.copy(
            id = tokenId,
            accessToken = "Updated Access Token",
            deviceId = "Updated Device ID"
        )
        tokenDao.updateUserlessToken(updatedUserlessToken)

        //Assert
        tokenDao.getUserlessToken()
            .test()
            .assertResult(updatedUserlessToken)
    }

    @Test
    fun setAndGetCurrentToken() {
        //Arrange
        val token = createUserToken()
        val rowId = tokenDao.saveUserToken(token)
        val expectedToken = token.copy(rowId)
        tokenDao.setCurrentToken(expectedToken)
            .test()
            .assertResult()

        //Act, Assert
        tokenDao.getCurrentToken()
            .test()
            .assertResult(expectedToken)
    }
}