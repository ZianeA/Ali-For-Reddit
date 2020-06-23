package com.visualeap.aliforreddit.domain.subreddit

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface SubredditRepository {
    fun addSubreddit(subreddit: Subreddit): Completable
    fun addSubreddits(subreddits: List<Subreddit>): Completable
    fun getSubredditsByIds(ids: List<String>): Flowable<List<Subreddit>>
    fun getSubredditByPost(postId: String): Observable<Subreddit>
    fun updateSubreddit(subreddit: Subreddit): Completable
}