package com.visualeap.aliforreddit.data.cache.redditor

import androidx.room.Dao
import com.visualeap.aliforreddit.data.repository.redditor.RedditorLocalSource
import com.visualeap.aliforreddit.domain.model.Redditor
import io.reactivex.Single

@Dao
abstract class RedditorDao: RedditorLocalSource {
    override fun getRedditor(username: String): Single<Redditor> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}