package com.visualeap.aliforreddit.domain.repository

import com.visualeap.aliforreddit.domain.model.Subreddit
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable

interface SubredditRepository {
    fun addSubreddit(subreddit: Subreddit): Completable
    fun addSubreddits(subreddits: List<Subreddit>): Completable
    fun getSubredditsByIds(ids: List<String>): Flowable<List<Subreddit>>
}