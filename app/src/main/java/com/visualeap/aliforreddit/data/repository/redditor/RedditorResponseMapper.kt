package com.visualeap.aliforreddit.data.repository.redditor

import com.visualeap.aliforreddit.data.repository.Mapper
import com.visualeap.aliforreddit.domain.model.Redditor
import dagger.Reusable
import javax.inject.Inject

@Reusable
class RedditorResponseMapper @Inject constructor(): Mapper<RedditorResponse, Redditor> {
    override fun map(model: RedditorResponse): Redditor {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mapReverse(model: Redditor): RedditorResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}