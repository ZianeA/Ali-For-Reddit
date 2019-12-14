package com.visualeap.aliforreddit.presentation.main

import com.visualeap.aliforreddit.domain.model.Redditor
import com.visualeap.aliforreddit.presentation.di.ActivityScope

interface MainView {
    fun displayCurrentRedditor(redditor: Redditor)
    fun displayLoginPrompt()
}
