package com.visualeap.aliforreddit.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Subreddit(
    val name: String,
    val id: String,
    val iconUrl: String?,
    val primaryColor: String?,
    val keyColor: String?
) : Parcelable
