package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.util.createBasicAuth
import com.visualeap.aliforreddit.util.createUserlessToken
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GetUserLessTokenTest {
    private val authService: AuthService = mockk()
    private val basicAuth = createBasicAuth()
    private val getUserLessToken = GetUserLessToken(authService, basicAuth)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `return user-less token`() {
        //Arrange
        val deviceId = "DEVICE ID"
        val expectedToken = createUserlessToken()
        every {
            authService.getUserLessToken(
                "https://oauth.reddit.com/grants/installed_client",
                deviceId,
                basicAuth
            )
        } returns Single.just(expectedToken)

        //Act
        val token = getUserLessToken.execute(deviceId)

        //Assert
        assertThat(token).isEqualTo(expectedToken)
    }

    @Test
    fun `return null when token retrieval fails`() {
        //Arrange
        every {
            authService.getUserLessToken(
                any(),
                any(),
                any()
            )
        } returns Single.error(Throwable())

        //Act
        val token = getUserLessToken.execute("")

        //Assert
        assertThat(token).isNull()
    }
}