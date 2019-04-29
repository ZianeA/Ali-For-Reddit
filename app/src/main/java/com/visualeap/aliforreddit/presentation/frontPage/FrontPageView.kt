package com.visualeap.aliforreddit.presentation.frontPage

import net.dean.jraw.models.Submission

interface FrontPageView {
    fun displaySubmissions(submissions: List<Submission>)
}
