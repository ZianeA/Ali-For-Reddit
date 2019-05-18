package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.entity.Credentials
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.assertj.core.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetAuthUrlTest {

    private val getAuthenticationUrl = GetAuthUrl()

    @Test
    fun `should return correct authentication url`() {
        //Arrange
        val expectedAuthUrl =
            "https://www.reddit.com/api/v1/authorize.compact?client_id=CLIENT_ID&response_type=code" +
                    "&state=RANDOM_STRING&redirect_uri=URI&duration=permanent&scope="

        //Act
        val actualAuthUrl = getAuthenticationUrl.execute(
            GetAuthUrl.Params(
                Credentials("CLIENT_ID", "URI"),
                "RANDOM_STRING"
            )
        )

        //Assert
        assertThat(actualAuthUrl).contains(expectedAuthUrl)
    }
}