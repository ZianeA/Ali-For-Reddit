package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.repository.TokenRepository
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Maybe
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createUserToken
import util.domain.createUserlessToken

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class IsUserLoggedInTest {
    private val tokenRepository: TokenRepository = mockk()
    private val isUserLoggedIn = IsUserLoggedIn(tokenRepository)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `return true if the current token is a UserToken`() {
        //Arrange
        every { tokenRepository.getCurrentToken() } returns Maybe.just(createUserToken())

        //Act, assert
        isUserLoggedIn.execute()
            .test()
            .assertResult(true)
    }

    @Test
    fun `return false if the current token is a UserlessToken`() {
        //Arrange
        every { tokenRepository.getCurrentToken() } returns Maybe.just(createUserlessToken())

        //Act, assert
        isUserLoggedIn.execute()
            .test()
            .assertResult(false)
    }

    @Test
    fun `return false if there's no current token`() {
        //Arrange
        every { tokenRepository.getCurrentToken() } returns Maybe.empty()

        //Act, assert
        isUserLoggedIn.execute()
            .test()
            .assertResult(false)
    }
}