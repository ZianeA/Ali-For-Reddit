package com.visualeap.aliforreddit.presentation.util

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.main.frontPage.SubredditIcon

object SubredditFormatter {
    private const val SUBREDDIT_DEFAULT_COLOR = "#33a8ff"

    fun formatIcon(iconUrl: String?): SubredditIcon {
        return if (iconUrl != null) SubredditIcon.Custom(iconUrl)
        else SubredditIcon.Default(R.drawable.ic_subreddit_default)
    }

    fun formatColor(primaryColor: String?, keyColor: String?): String {
        return primaryColor ?: keyColor ?: SUBREDDIT_DEFAULT_COLOR
    }
}