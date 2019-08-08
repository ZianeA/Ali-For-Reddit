package com.visualeap.aliforreddit.domain.usecase

//TODO these maybe useful when designing TokenRepository unit tests
/*
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GetUserLessTokenTest {
    private val tokenRepository: TokenRepository = mockk()
    private val basicAuth = createBasicAuth()
    private val getUserLessToken = GetUserLessToken(tokenRepository, basicAuth)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `return user-less token`() {
        //Arrange
        val deviceId = "DEVICE ID"
        every {
            tokenRepository.getUserLessToken(
                "https://oauth.reddit.com/grants/installed_client",
                deviceId,
                basicAuth
            )
        } returns Single.just(createUserlessToken(deviceId = null))

        //Act
        val token = getUserLessToken.execute(deviceId)

        //Assert
        val expectedToken = createUserlessToken(deviceId = deviceId)
        assertThat(token).isEqualToComparingFieldByField(expectedToken)
    }

    @Test
    fun `return null when token retrieval fails`() {
        //Arrange
        every {
            tokenRepository.getUserLessToken(
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
}*/
