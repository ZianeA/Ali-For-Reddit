package com.visualeap.aliforreddit.domain.usecase

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.assertj.core.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BuildAuthUrlTest {

    companion object {
        private const val REDIRECT_URL = "URI"
        private const val CLIENT_ID = "CLIENT_ID"
    }

    private val getAuthenticationUrl = BuildAuthUrl(CLIENT_ID, REDIRECT_URL)

    @Test
    fun `should return correct authentication url`() {
        //Arrange
        val expectedAuthUrl =
            "https://www.reddit.com/api/v1/authorize.compact?client_id=CLIENT_ID&response_type=code" +
                    "&state=RANDOM_STRING&redirect_uri=URI&duration=permanent&scope="

        //Act
        val actualAuthUrl = getAuthenticationUrl.execute("RANDOM_STRING")

        //Assert
        assertThat(actualAuthUrl).contains(expectedAuthUrl)
    }
}