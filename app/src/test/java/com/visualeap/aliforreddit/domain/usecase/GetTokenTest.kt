package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.repository.AccountRepository
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import com.visualeap.aliforreddit.util.*
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import org.assertj.core.api.Assertions.*
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
        every { tokenRepository.getUserLessToken(any()) } returns Single.just(createUserlessToken())

        //Act, Assert
        getToken.execute(Unit)
            .test()
            .assertResult(expectedToken)
    }

    @Test
    fun `return user-less token when there's no current token`() {
        //Arrange
        val expectedToken = createUserlessToken()
        every { tokenRepository.getCurrentToken() } returns Maybe.empty()
        every { tokenRepository.getUserLessToken(any()) } returns Single.just(expectedToken)

        //Act, Assert
        getToken.execute(Unit)
            .test()
            .assertResult(expectedToken)
    }

    //TODO Move this test to tokenRepository, because it's the one responsible for caching tokens
//    @Test
//    fun `store user-less token after retrieval`() {
//        //Arrange
//        val expectedToken = createUserlessToken()
//        val expectedAccount = createAnonymousAccount(expectedToken)
//
//        every { getCurrentAccount.execute(Unit) }.returnsMany(null, expectedAccount)
//        every { getUserLessToken.execute(any()) } returns expectedToken
//        every { accountRepository.saveAccount(any()) } returns Completable.complete()
//
//        //Act
//        var token: Token? = null
//
//        repeat(3) {
//            token = getToken.execute(Unit)
//        }
//
//        //Assert
//        //Fetch the user-less token from the server only once
//        verify(atMost = 1) { getUserLessToken.execute(any()) }
//
//        //Save the user-less token only once, and Create the correct Anonymous account
//        verify(atMost = 1) {
//            accountRepository.saveAccount(
//                withArg {
//                    assertThat(it).isEqualToIgnoringGivenFields(expectedAccount, "deviceId")
//                })
//        }
//
//        //Return the correct token
//        assertThat(token).isEqualToIgnoringGivenFields(expectedToken, "deviceId")
//    }

    //TODO Move this test to tokenRepository, because it's the one responsible for caching tokens
//    @Test
//    fun `throw an exception when saving token fails`() {
//        //Arrange
//        every { getCurrentAccount.execute(Unit) } returns null
//        every { getUserLessToken.execute(any()) } returns createUserlessToken()
//        every { accountRepository.saveAccount(any()) } returns Completable.error(SQLException())
//
//        //Act, Assert
//        assertThatThrownBy { getToken.execute(Unit) }.isInstanceOf(SQLException::class.java)
//    }
}