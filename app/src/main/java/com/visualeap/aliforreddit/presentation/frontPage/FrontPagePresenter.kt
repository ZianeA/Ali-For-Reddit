package com.visualeap.aliforreddit.presentation.frontPage

import com.visualeap.aliforreddit.domain.repository.SubmissionRepository

class FrontPagePresenter(private val view : FrontPageView, private val repository : SubmissionRepository) {

    fun loadSubmissions() {
        val submissions = repository.getSubmissions()
        view.displaySubmissions(submissions)
    }
}