package com.visualeap.aliforreddit.data.repository.account

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.visualeap.aliforreddit.data.repository.redditor.RedditorEntity
import com.visualeap.aliforreddit.data.repository.token.TokenEntity

@Entity
data class AccountEntity(
    @PrimaryKey val username: String
)
