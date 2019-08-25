package com.visualeap.aliforreddit.data.repository.redditor

import com.visualeap.aliforreddit.domain.model.Redditor
import com.visualeap.aliforreddit.domain.repository.RedditorRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedditorDataRepository @Inject constructor(): RedditorRepository {
    override fun getCurrentRedditor(): Single<Redditor> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
