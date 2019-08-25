package com.visualeap.aliforreddit.data.repository.redditor

import com.visualeap.aliforreddit.data.cache.redditor.RedditorEntity
import com.visualeap.aliforreddit.domain.model.Redditor
import io.reactivex.Single

interface RedditorLocalSource {
    fun getRedditor(username: String): Single<Redditor>
}
