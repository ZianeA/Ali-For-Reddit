package com.visualeap.aliforreddit.data.cache.account

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.visualeap.aliforreddit.data.cache.RedditDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import util.domain.NOT_SET_ROW_ID
import util.domain.createAccount
import util.domain.randomInteger

@RunWith(AndroidJUnit4::class)
internal class AccountDaoTest {
    private lateinit var accountDao: AccountDao
    private lateinit var db: RedditDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getContext()
        db = Room.inMemoryDatabaseBuilder(context, RedditDatabase::class.java)
            .build()
        accountDao = db.accountDao()
        //TODO add a redditor and token record
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetAccountEntity() {
        //Arrange
        val account = AccountEntity(NOT_SET_ROW_ID, "RandomUsername", randomInteger)

        //Act
        accountDao.addAccountEntity(account)
            .test()
            .assertResult()

        //Assert
        accountDao.getAllAccountEntities()
            .test()
            .assertValue { it.size == 1 && it.first() == account.copy(id = 1) }
    }
}