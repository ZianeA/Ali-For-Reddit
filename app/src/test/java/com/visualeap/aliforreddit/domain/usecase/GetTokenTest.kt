package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.repository.TokenRepository
import util.*
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.sql.SQLException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetTokenTest {
    private val tokenRepository: TokenRepository = mockk()
    private val getToken = GetToken(tokenRepository)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `return current token`() {
        //Arrange
        val expectedToken = createToken()
        every { tokenRepository.getCurrentToken() } returns Maybe.just(expectedToken)
        every { tokenRepository.getUserlessToken(any()) } returns Single.just(createUserlessToken())
        every { tokenRepository.setCurrentToken(any()) } returns Completable.complete()

        //Act, Assert
        getToken.execute(Unit)
            .test()
            .assertResult(expectedToken)
    }

    @Test
    fun `return user-less token when there's no current token`() {
        //Arrange
        val expectedToken = createUserlessToken()
        every { tokenRepository.getCurrentToken() }.returnsMany(
            Maybe.empty(),
            Maybe.just(expectedToken)
        )
        every { tokenRepository.getUserlessToken(any()) } returns Single.just(expectedToken)
        every { tokenRepository.setCurrentToken(any()) } returns Completable.complete()

        //Act, Assert
        getToken.execute(Unit)
            .test()
            .assertResult(expectedToken)
    }

    @Test
    fun `set the fetched user-less token as the current token`() {
        //Arrange
        val fetchedToken = createUserlessToken()
        every { tokenRepository.getCurrentToken() } returns Maybe.empty()
        every { tokenRepository.getUserlessToken(any()) } returns Single.just(fetchedToken)

        //Act
        getToken.execute(Unit)
            .test()

        //Assert
        verify { tokenRepository.setCurrentToken(fetchedToken) }
    }

    @Test
    fun `return error when setting the fetched user-less token as the current token fails`() {
        //Arrange
        val fetchedToken = createUserlessToken()
        every { tokenRepository.getCurrentToken() } returns Maybe.empty()
        every { tokenRepository.getUserlessToken(any()) } returns Single.just(fetchedToken)
        every { tokenRepository.setCurrentToken(any()) } returns Completable.error(SQLException())

        //Act, Assert
        getToken.execute(Unit)
            .test()
            .assertFailure(SQLException::class.java)
    }
}