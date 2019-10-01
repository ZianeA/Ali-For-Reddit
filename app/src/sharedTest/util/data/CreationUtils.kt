package util.data

import com.visualeap.aliforreddit.data.cache.token.TokenEntity
import com.visualeap.aliforreddit.data.cache.token.UserTokenEntity
import com.visualeap.aliforreddit.data.cache.token.UserlessTokenEntity
import util.domain.*

fun createTokenEntity(
    id: Int = ID,
    accessToken: String = ACCESS_TOKEN,
    type: String = TOKEN_TYPE
) = TokenEntity(id, accessToken, type)

fun createUserTokenEntity(id: Int = ID, refreshToken: String = REFRESH_TOKEN) =
    UserTokenEntity(id, refreshToken)

fun createUserlessTokenEntity(id: Int = ID, deviceId: String = DEVICE_ID) =
    UserlessTokenEntity(id, deviceId)