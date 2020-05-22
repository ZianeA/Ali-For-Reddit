package com.visualeap.aliforreddit.data.repository.token

import androidx.room.*
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface UserlessTokenDao {
    @Query("SELECT id FROM UserlessTokenEntity")
    fun getUserlessTokenId(): Maybe<Int>

    @Query("SELECT * FROM UserlessTokenEntity u INNER JOIN TokenEntity t ON u.id = t.id")
    fun getUserlessToken(): Single<UserlessToken>

    @Insert
    fun addUserlessTokenEntity(token: UserlessTokenEntity)

    @Update
    fun updateUserlessTokenEntity(token: UserlessTokenEntity)
}