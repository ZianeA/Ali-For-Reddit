package com.visualeap.aliforreddit.util

import com.visualeap.aliforreddit.domain.model.Account
import com.visualeap.aliforreddit.domain.model.User
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import okhttp3.Credentials
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response

fun createUserToken(
    accessToken: String = "ACCESS TOKEN",
    type: String = "bearer",
    refreshToken: String? = "REFRESH TOKEN"
) = UserToken(accessToken, type, refreshToken)

fun createUserlessToken(
    accessToken: String = "ACCESS TOKEN",
    type: String = "bearer",
    deviceId: String? = "DEVICE ID"
) = UserlessToken(accessToken, type, deviceId)

fun createToken(): Token = createUserToken()

fun createAccount(
    username: String = "Test",
    token: Token = createUserToken(),
    isLoggedIn: Boolean = true,
    avatarUrl: String? = "https://www.example.com/test.png"
) = Account(username, token, isLoggedIn, avatarUrl)

fun createUser(name: String = "Test", avatarUrl: String = "https://example.com/test.png"): User =
    User(name, avatarUrl)

fun createAnonymousAccount(
    token: Token = createUserlessToken(),
    isLoggedIn: Boolean = true
) = createAccount("Anonymous", token, isLoggedIn, null)

fun createBasicAuth(clientId: String = "CLIENT ID"): String = Credentials.basic(clientId, "")

fun createResponse(request: Request = createRequest()): Response {
    return Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_2)
        .code(401)
        .message("")
        .build()
}

fun createRequest(): Request = Request.Builder()
    .url("https://www.example.com")
    .build()