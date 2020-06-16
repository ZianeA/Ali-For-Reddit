package com.visualeap.aliforreddit.presentation.main.frontPage

import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.presentation.model.PostView

interface FrontPageView {
    fun render(viewState: FrontPageViewState)
}
