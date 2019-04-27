package com.visualeap.aliforreddit.domain.repository

import com.visualeap.aliforreddit.domain.entity.Submission

interface SubmissionRepository {

    fun getSubmissions() : List<Submission>
}
