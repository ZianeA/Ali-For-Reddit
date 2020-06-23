package com.visualeap.aliforreddit.domain.subreddit

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Subreddit(
    val id: String,
    val name: String,
    val iconUrl: String?,
    val primaryColor: String?,
    val keyColor: String?
)
