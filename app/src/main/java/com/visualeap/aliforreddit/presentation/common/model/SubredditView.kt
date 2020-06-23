package com.visualeap.aliforreddit.presentation.common.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubredditView(
    val id: String,
    val name: String,
    val iconUrl: String?,
    val color: String
) : Parcelable