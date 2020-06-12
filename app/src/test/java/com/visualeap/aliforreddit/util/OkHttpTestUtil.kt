package com.visualeap.aliforreddit.util

import io.mockk.every
import io.mockk.mockk
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response

//region OkHttp Chain
fun createResponse(request: Request = createRequest()): Response {
    return Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_2)
        .code(401)
        .message("")
        .build()
}

fun createMockChain(): Interceptor.Chain {
    val chain: Interceptor.Chain = mockk()

    every { chain.request() } returns createRequest()
    every { chain.proceed(any()) } answers { createResponse(firstArg()) }

    return chain
}

fun createRequest(): Request {
    return Request.Builder()
        .url("https://www.example.com")
        .build()
}
//endregion