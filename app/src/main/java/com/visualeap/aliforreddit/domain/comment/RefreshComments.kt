package com.visualeap.aliforreddit.domain.comment

import dagger.Reusable
import io.reactivex.Completable
import javax.inject.Inject

@Reusable
class RefreshComments @Inject constructor() {
    fun execute(postId: String): Completable {
        TODO("Not implemented")
    }
}