package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.SyncSchedulerProvider
import com.visualeap.aliforreddit.domain.entity.Credentials
import com.visualeap.aliforreddit.domain.entity.Token
import com.visualeap.aliforreddit.util.createToken
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.reactivex.Single
import okhttp3.HttpUrl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import java.net.MalformedURLException
import kotlin.IllegalArgumentException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class AuthenticateUserTest {

    companion object {
        private const val CLIENT_ID = "CLIENT ID"
        private const val REDIRECT_URL = "https://example.com/path"
        private const val CODE = "CODE"
        private const val STATE = "STATE"
    }

    private val authService: AuthService = mockk()
    private val authenticateUser = AuthenticateUser(SyncSchedulerProvider())
    private val token = createToken()

    init {
        every {
            authService.getAccessToken(
                "authorization_code",
                CODE,
                REDIRECT_URL,
                okhttp3.Credentials.basic(CLIENT_ID, "")
            )
        } returns Single.just(token)
    }

    @Test
    fun `return access token`() {
        //Arrange
        val finalUrl = HttpUrl.parse(REDIRECT_URL)!!
            .newBuilder()
            .addQueryParameter("code", CODE)
            .addQueryParameter("state", STATE)
            .build()
            .toString()

        val params = createParams(finalUrl)

        //Act, Assert
        authenticateUser.execute(params)
            .test()
            .assertResult(token)
    }

    @Test
    fun `return error when the final redirect url is malformed`() {
        //Arrange
        val finalUrl = "Malformed URL"
        val params = createParams(finalUrl)

        //Act, Assert
        authenticateUser.execute(params)
            .test()
            .assertFailure(MalformedURLException::class.java)
    }

    @Test
    fun `return error when final redirect url contains error query`() {
        //Arrange
        val finalUrl = HttpUrl.parse(REDIRECT_URL)!!
            .newBuilder()
            .addQueryParameter("error", "ERROR")
            .build()
            .toString()

        val params = createParams(finalUrl)

        //Act, Assert
        authenticateUser.execute(params)
            .test()
            .assertFailure(OAuthException::class.java)
    }

    @Test
    fun `return error when final redirect url doesn't contain code`() {
        //Arrange
        val finalUrl = HttpUrl.parse(REDIRECT_URL)!!
            .newBuilder()
            .addQueryParameter("code", CODE)
            .build()
            .toString()

        val params = createParams(finalUrl)

        //Act, Assert
        authenticateUser.execute(params)
            .test()
            .assertFailure(IllegalArgumentException::class.java)
    }

    @Test
    fun `return error when final redirect url doesn't contain state`() {
        //Arrange
        val finalUrl = HttpUrl.parse(REDIRECT_URL)!!
            .newBuilder()
            .addQueryParameter("code", CODE)
            .build()
            .toString()

        val params = createParams(finalUrl)

        //Act, Assert
        authenticateUser.execute(params)
            .test()
            .assertFailure(IllegalArgumentException::class.java)
    }

    @Test
    fun `return error when state doesn't match`() {
        //Arrange
        val finalUrl = HttpUrl.parse(REDIRECT_URL)!!
            .newBuilder()
            .addQueryParameter("code", CODE)
            .addQueryParameter("state", "Invalid state")
            .build()
            .toString()

        val params = createParams(finalUrl)

        //Act, Assert
        authenticateUser.execute(params)
            .test()
            .assertFailure(IllegalStateException::class.java)
    }

    private fun createParams(finalUrl: String) = AuthenticateUser.Params(
        authService,
        finalUrl,
        Credentials(CLIENT_ID, REDIRECT_URL),
        STATE
    )
}