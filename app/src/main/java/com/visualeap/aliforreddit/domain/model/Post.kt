package com.visualeap.aliforreddit.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Post(
    val id: String,
    val authorName: String,
    val title: String,
    val text: String,
    val score: Int,
    val commentCount: Int,
    val subreddit: Subreddit,
    val created: Long
)
