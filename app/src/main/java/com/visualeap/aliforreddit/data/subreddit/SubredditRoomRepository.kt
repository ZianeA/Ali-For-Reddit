package com.visualeap.aliforreddit.data.subreddit

import com.visualeap.aliforreddit.domain.subreddit.Subreddit
import com.visualeap.aliforreddit.domain.subreddit.SubredditRepository
import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

@Reusable
class SubredditRoomRepository @Inject constructor(private val subredditDao: SubredditDao) :
    SubredditRepository {
    override fun addSubreddit(subreddit: Subreddit): Completable {
        return subredditDao.add(toEntity(subreddit))
    }

    override fun addSubreddits(subreddits: List<Subreddit>): Completable {
        return subredditDao.addAll(subreddits.map(::toEntity))
    }

    override fun getSubredditsByIds(ids: List<String>): Flowable<List<Subreddit>> {
        return subredditDao.getByIds(ids).map { subredditList -> subredditList.map(::toDomain) }
    }

    override fun getSubredditByPost(postId: String): Observable<Subreddit> {
        return subredditDao.getByPost(postId).map(::toDomain)
    }

    override fun updateSubreddit(subreddit: Subreddit): Completable {
        return subredditDao.update(toEntity(subreddit))
    }

    private fun toEntity(subreddit: Subreddit): SubredditEntity {
        return subreddit.run { SubredditEntity(id, name, iconUrl, primaryColor, keyColor) }
    }

    private fun toDomain(subreddit: SubredditEntity): Subreddit {
        return subreddit.run {
            Subreddit(
                id,
                name,
                iconUrl?.takeIf { it.isNotBlank() }, // When a string is null, Room returns an empty string.
                primaryColor?.takeIf { it.isNotBlank() },
                keyColor?.takeIf { it.isNotBlank() }
            )
        }
    }
}