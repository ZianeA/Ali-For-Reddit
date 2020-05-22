package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.util.UniqueStringGenerator
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.assertj.core.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BuildAuthUrlTest {
    private val uniqueStringGenerator: UniqueStringGenerator = mockk()
    private val getAuthenticationUrl = BuildAuthUrl(uniqueStringGenerator)

    @Test
    fun `should return correct authentication url`() {
        //Arrange
        val state = "RANDOM_STRING"
        every { uniqueStringGenerator.generate() } returns state

        val clientId = "CLIENT_ID"
        val redirectUrl = "URI"
        val expectedAuthUrl =
            "https://www.reddit.com/api/v1/authorize.compact?client_id=$clientId&response_type=code" +
                    "&state=$state&redirect_uri=$redirectUrl&duration=permanent&scope="

        //Act
        val actualAuthUrl = getAuthenticationUrl.execute(clientId, redirectUrl)

        //Assert
        assertThat(actualAuthUrl).contains(expectedAuthUrl)
    }
}