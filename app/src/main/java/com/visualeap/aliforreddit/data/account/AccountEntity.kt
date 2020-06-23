package com.visualeap.aliforreddit.data.account

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AccountEntity(
    @PrimaryKey val username: String
)
