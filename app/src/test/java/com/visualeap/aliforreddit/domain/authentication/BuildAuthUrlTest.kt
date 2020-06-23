package com.visualeap.aliforreddit.domain.authentication

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.assertj.core.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BuildAuthUrlTest {
    private val getAuthenticationUrl =
        BuildAuthUrl()

    @Test
    fun `should return correct authentication url`() {
        //Arrange
        val clientId = "CLIENT_ID"
        val redirectUrl = "URI"

        //Act
        val authUrl = getAuthenticationUrl.execute(clientId, redirectUrl)

        //Assert
        assertThat(authUrl).contains("https://www.reddit.com/api/v1/authorize.compact")
            .contains("client_id=$clientId")
            .contains("response_type=code")
            .contains("redirect_uri=$redirectUrl")
            .contains("duration=permanent")
            .contains("state=")
    }
}