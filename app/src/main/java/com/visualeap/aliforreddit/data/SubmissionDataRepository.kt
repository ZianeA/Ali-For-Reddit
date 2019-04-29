package com.visualeap.aliforreddit.data

import android.util.Log
import com.visualeap.aliforreddit.core.AliForRedditApp
import com.visualeap.aliforreddit.domain.repository.SubmissionRepository
import io.reactivex.Observable
import net.dean.jraw.models.Submission
import net.dean.jraw.oauth.StatefulAuthHelper

class SubmissionDataRepository : SubmissionRepository {

    override fun getSubmissions(): Observable<List<Submission>> {
        return Observable.fromCallable {
            Log.i(SubmissionDataRepository::class.java.simpleName, Thread.currentThread().name)

            val redditClient = AliForRedditApp.accountHelper.reddit
            val paginatorBuilder = redditClient.frontPage()
            val paginator = paginatorBuilder.build()

            val submissions = paginator.next()
            Log.d(SubmissionDataRepository::class.java.simpleName, submissions.nextName)

            return@fromCallable submissions
        }
    }
}