package com.visualeap.aliforreddit.data.repository.subreddit

import com.visualeap.aliforreddit.data.repository.Mapper
import com.visualeap.aliforreddit.domain.model.Subreddit
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SubredditEntityMapper @Inject constructor() : Mapper<SubredditEntity, Subreddit> {
    override fun map(model: SubredditEntity): Subreddit {
        return model.run { Subreddit(id, name) }
    }

    override fun mapReverse(model: Subreddit): SubredditEntity {
        return model.run { SubredditEntity(id, name) }
    }
}