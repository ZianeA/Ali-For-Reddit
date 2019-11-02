package com.visualeap.aliforreddit.data.repository.token

import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.domain.model.token.UserToken
import dagger.Reusable
import javax.inject.Inject

@Reusable
class TokenWithUserTokenEntityMapper @Inject constructor() :
    Mapper<TokenWithUserTokenEntity, UserToken> {
    override fun map(model: TokenWithUserTokenEntity): UserToken = model.run {
        UserToken(
            tokenEntity.id,
            tokenEntity.accessToken,
            tokenEntity.type,
            userTokenEntity.refreshToken
        )
    }

    override fun mapReverse(model: UserToken): TokenWithUserTokenEntity = model.run {
        TokenWithUserTokenEntity(
            TokenEntity(id, accessToken, type),
            UserTokenEntity(id, refreshToken)
        )
    }
}