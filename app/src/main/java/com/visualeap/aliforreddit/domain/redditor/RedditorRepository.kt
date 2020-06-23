package com.visualeap.aliforreddit.domain.redditor

import io.reactivex.Single

interface RedditorRepository {
    fun getCurrentRedditor(): Single<Redditor>
}
