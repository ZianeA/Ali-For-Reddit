package com.visualeap.aliforreddit.domain.repository

import io.reactivex.Observable
import net.dean.jraw.models.Submission

interface SubmissionRepository {

    fun getSubmissions(): Observable<List<Submission>>
}
