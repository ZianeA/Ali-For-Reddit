package com.visualeap.aliforreddit.presentation.common.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostView(
    val id: String,
    val authorName: String,
    val title: String,
    val text: String,
    val score: String,
    val commentCount: String,
    val subreddit: SubredditView,
    val timestamp: String
) : Parcelable