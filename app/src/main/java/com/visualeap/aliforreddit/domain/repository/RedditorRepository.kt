package com.visualeap.aliforreddit.domain.repository

import com.visualeap.aliforreddit.domain.model.Redditor
import io.reactivex.Single

interface RedditorRepository {
    fun getCurrentRedditor(): Single<Redditor>
}
