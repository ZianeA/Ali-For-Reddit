package com.visualeap.aliforreddit.presentation.common.main

import com.visualeap.aliforreddit.domain.redditor.Redditor

interface MainView {
    fun displayCurrentRedditor(redditor: Redditor)
    fun displayLoginPrompt()
}
