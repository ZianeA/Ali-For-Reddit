package com.visualeap.aliforreddit.data.repository.token

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.visualeap.aliforreddit.domain.model.token.UserToken
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface UserTokenDao {
    @Query("SELECT * FROM UserTokenEntity u INNER JOIN TokenEntity t ON u.id = t.id WHERE u.id =:id")
    fun getUserToken(id: Int): Single<UserToken>

    @Insert
    fun addUserTokenEntity(token: UserTokenEntity)

    @Update
    fun updateUserTokenEntity(token: UserTokenEntity)
}