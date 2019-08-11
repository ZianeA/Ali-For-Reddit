package util

import com.visualeap.aliforreddit.domain.model.Account
import com.visualeap.aliforreddit.domain.model.Redditor
import com.visualeap.aliforreddit.domain.model.token.Token
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import okhttp3.Credentials
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import kotlin.random.Random

const val ACCESS_TOKEN = "ACCESS TOKEN"
const val TOKEN_TYPE = "bearer"
const val REFRESH_TOKEN = "REFRESH TOKEN"
const val DEVICE_ID = "DEVICE ID"
const val REDIRECT_URL = "https://example.com/path"
const val CODE = "CODE"
const val ID = 101
const val NOT_SET_ROW_ID = 0

fun createUserToken(
    id: Int = ID,
    accessToken: String = ACCESS_TOKEN,
    type: String = TOKEN_TYPE,
    refreshToken: String = REFRESH_TOKEN
) = UserToken(id, accessToken, type, refreshToken)

fun createUserlessToken(
    id: Int = ID,
    accessToken: String = ACCESS_TOKEN,
    type: String = TOKEN_TYPE,
    deviceId: String = DEVICE_ID
) = UserlessToken(id, accessToken, type, deviceId)

fun createToken(
    id: Int = ID,
    accessToken: String = ACCESS_TOKEN,
    type: String = TOKEN_TYPE
): Token {
    return if (Random.nextBoolean()) createUserlessToken(id, accessToken, type)
    else createUserToken(id, accessToken, type)
}

fun createAccount(
    id: Int = ID,
    redditor: Redditor = createRedditor(),
    token: UserToken = createUserToken()
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