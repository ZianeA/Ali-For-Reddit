package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.util.BasicAuthCredentialProvider
import com.visualeap.aliforreddit.presentation.common.ResourceProvider
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import okhttp3.Credentials
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class BasicAuthCredentialProviderTest {
    private val resourceProvider: ResourceProvider = mockk()
    private val authProvider =
        BasicAuthCredentialProvider(
            resourceProvider
        )

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `should return auth credential for the Basic scheme`() {
        //Arrange
        val clientId = "CLIENT_ID"
        every { resourceProvider.getString(R.string.client_id) } returns clientId

        //Act
        val authCredential = authProvider.getAuthCredential()

        //Assert
        val expectedAuthCredential = Credentials.basic(clientId, "")
        assertThat(authCredential).isEqualTo(expectedAuthCredential)
    }
}