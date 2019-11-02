package com.visualeap.aliforreddit.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubredditView(
    val name: String,
    val id: String,
    val iconUrl: String?,
    val color: String
) : Parcelable