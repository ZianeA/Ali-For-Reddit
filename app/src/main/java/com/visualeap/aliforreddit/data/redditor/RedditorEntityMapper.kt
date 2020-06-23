package com.visualeap.aliforreddit.data.redditor

import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.domain.redditor.Redditor
import dagger.Reusable
import javax.inject.Inject

@Reusable
class RedditorEntityMapper @Inject constructor() :
    Mapper<RedditorEntity, Redditor> {
    override fun map(model: RedditorEntity): Redditor {
        return model.run {
            Redditor(
                id,
                username,
                creationDate,
                linkKarma,
                commentKarma,
                iconUrl,
                coins
            )
        }
    }

    override fun mapReverse(model: Redditor): RedditorEntity {
        return model.run {
            RedditorEntity(
                id,
                username,
                creationDate,
                linkKarma,
                commentKarma,
                iconUrl,
                coins
            )
        }
    }
}