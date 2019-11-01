package com.visualeap.aliforreddit.data.repository.token

import androidx.room.Embedded
import androidx.room.Relation

data class TokenWithUserTokenEntity(
    @Embedded
    val tokenEntity: TokenEntity,
    @Relation(parentColumn = "id", entityColumn = "id")
    val userTokenEntity: UserTokenEntity
)