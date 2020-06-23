package com.visualeap.aliforreddit.presentation.frontPage

import androidx.annotation.DrawableRes

sealed class SubredditIcon() {
    data class Custom(val url: String) : SubredditIcon()
    data class Default(@DrawableRes val resId: Int) : SubredditIcon()
}