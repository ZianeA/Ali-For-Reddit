package com.visualeap.aliforreddit.util

import com.visualeap.aliforreddit.domain.entity.Token
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response

fun createToken(
    accessToken: String = "ACCESS TOKEN",
    type: String = "bearer",
    refreshToken: String? = "REFRESH TOKEN"
) = Token(accessToken, type, refreshToken)

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