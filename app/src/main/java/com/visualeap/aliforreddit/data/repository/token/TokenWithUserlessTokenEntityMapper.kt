package com.visualeap.aliforreddit.data.repository.token

import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import dagger.Reusable
import javax.inject.Inject

@Reusable
class TokenWithUserlessTokenEntityMapper @Inject constructor() :
    Mapper<TokenWithUserlessTokenEntity, UserlessToken> {
    override fun map(model: TokenWithUserlessTokenEntity): UserlessToken = model.run {
        UserlessToken(
            tokenEntity.id,
            tokenEntity.accessToken,
            tokenEntity.type,
            userlessTokenEntity.deviceId
        )
    }

    override fun mapReverse(model: UserlessToken): TokenWithUserlessTokenEntity = model.run {
        TokenWithUserlessTokenEntity(
            TokenEntity(id, accessToken, type),
            UserlessTokenEntity(id, deviceId)
        )
    }
}