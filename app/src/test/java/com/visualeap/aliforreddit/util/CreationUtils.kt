package com.visualeap.aliforreddit.util

import com.visualeap.aliforreddit.domain.model.Account
import com.visualeap.aliforreddit.domain.model.Redditor
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import okhttp3.Credentials
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response

val ACCESS_TOKEN = "ACCESS TOKEN"
val TOKEN_TYPE = "bearer"
val REFRESH_TOKEN = "REFRESH TOKEN"
val DEVICE_ID = "DEVICE ID"

fun createUserToken(
    id: Int = 1,
    accessToken: String = ACCESS_TOKEN,
    type: String = TOKEN_TYPE,
    refreshToken: String = REFRESH_TOKEN
) = UserToken(id, accessToken, type, refreshToken)

fun createUserlessToken(
    id: Int = 1,
    accessToken: String = ACCESS_TOKEN,
    type: String = TOKEN_TYPE,
    deviceId: String = DEVICE_ID
) = UserlessToken(id, accessToken, type, deviceId)

fun createToken(id: Int = 1, accessToken: String = ACCESS_TOKEN, type: String = TOKEN_TYPE): Token =
    Token(id, accessToken, type)

fun createAccount(
    id: Int = 1,
    redditor: Redditor = createRedditor(),
    token: Token = createToken()
) = Account(id, redditor, token)

fun createRedditor(username: String = "TestUser") = Redditor(username)

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