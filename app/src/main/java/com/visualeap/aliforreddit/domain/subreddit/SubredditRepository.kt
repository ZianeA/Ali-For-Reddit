package com.visualeap.aliforreddit.domain.subreddit

import io.reactivex.Completable
import io.reactivex.Flowable

interface SubredditRepository {
    fun addSubreddit(subreddit: Subreddit): Completable
    fun addSubreddits(subreddits: List<Subreddit>): Completable
    fun getSubredditsByIds(ids: List<String>): Flowable<List<Subreddit>>
}