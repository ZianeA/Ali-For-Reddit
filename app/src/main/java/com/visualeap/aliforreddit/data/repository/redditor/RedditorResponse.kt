package com.visualeap.aliforreddit.data.repository.redditor

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RedditorResponse(@Json(name = "data") val redditor: Redditor) {
    @JsonClass(generateAdapter = true)
    data class Redditor(@Json(name = "name") val username: String, val id: String)
}