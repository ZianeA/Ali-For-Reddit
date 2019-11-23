package com.visualeap.aliforreddit.data.repository.feed

import com.visualeap.aliforreddit.SyncSchedulerProvider
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class FeedRepositoryTest {
    private val feedDao: FeedDao = mockk()
    private val repository = FeedRepository(feedDao, SyncSchedulerProvider())

    /*@Test
    fun `add default feeds home, popular, and all`() {
        //Arrange
        val defaultFeeds = listOf(FeedEntity("HOME"), FeedEntity("POPULAR"), FeedEntity("ALL"))
        every { feedDao.getAll() } returns Single.just(emptyList())
        every { feedDao.addAll(any()) } returns Completable.complete()

        //Act
        repository.addDefaultFeeds()
            .test()
            .assertResult()

        //Assert
        verify { feedDao.addAll(defaultFeeds) }
    }

    @Test
    fun `add default feeds only once`() {
        //Arrange
        val defaultFeeds = listOf(FeedEntity("HOME"), FeedEntity("POPULAR"), FeedEntity("ALL"))
        every { feedDao.getAll() } returns Single.just(emptyList()) andThen Single.just(defaultFeeds)
        every { feedDao.addAll(any()) } returns Completable.complete()

        //Act
        repository.addDefaultFeeds()
            .andThen(repository.addDefaultFeeds())
            .test()
            .assertResult()

        //Assert
        verify(atMost = 1) { feedDao.addAll(any()) }
    }*/
}