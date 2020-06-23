package com.visualeap.aliforreddit.domain.post

import dagger.Reusable
import io.reactivex.Completable
import javax.inject.Inject

@Reusable
class RefreshPost @Inject constructor() {
    fun execute(postId: String): Completable {
        TODO("Not implemented")
    }
}