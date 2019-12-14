package com.visualeap.aliforreddit.data.repository.redditor

import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.domain.model.Redditor
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