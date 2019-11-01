package com.visualeap.aliforreddit.data.repository.token

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    foreignKeys = [ForeignKey(
        entity = TokenEntity::class,
        parentColumns = ["id"],
        childColumns = ["tokenId"],
        onDelete = CASCADE,
        onUpdate = CASCADE
    )]
)
data class CurrentTokenEntity(
    @PrimaryKey val id: Int,
    val tokenId: Int,
    val tokenType: TokenType
) {
    enum class TokenType { USER, USERLESS }

    class TokenTypeConverter {
        @TypeConverter
        fun fromTokenTypeToString(tokenType: TokenType) = tokenType.name

        @TypeConverter
        fun fromStringToTokenType(tokenType: String) = TokenType.valueOf(tokenType)
    }
}

