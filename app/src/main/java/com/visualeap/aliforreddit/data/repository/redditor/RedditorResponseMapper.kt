package com.visualeap.aliforreddit.data.repository.redditor

import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.domain.model.Redditor
import dagger.Reusable
import javax.inject.Inject

@Reusable
class RedditorResponseMapper @Inject constructor() :
    Mapper<RedditorResponse, Redditor> {
    override fun map(model: RedditorResponse): Redditor {
        return model.run {
            Redditor(id, username, creationDate, linkKarma, commentKarma, iconUrl, coins)
        }
    }

    override fun mapReverse(model: Redditor): RedditorResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}