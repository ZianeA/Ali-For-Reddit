package com.visualeap.aliforreddit.data.repository.subreddit

import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.domain.model.Subreddit
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SubredditEntityMapper @Inject constructor() :
    Mapper<SubredditEntity, Subreddit> {
    override fun map(model: SubredditEntity): Subreddit {
        return model.run {
            Subreddit(
                id,
                name,
                iconUrl,
                if (primaryColor.isNullOrEmpty() || primaryColor.isBlank()) null else primaryColor, //TODO Unit test or refactor
                if (keyColor.isNullOrEmpty() || keyColor.isBlank()) null else keyColor
            )
        }
    }

    override fun mapReverse(model: Subreddit): SubredditEntity {
        return model.run { SubredditEntity(id, name, iconUrl, primaryColor, keyColor) }
    }
}