package com.visualeap.aliforreddit.domain.model

import com.visualeap.aliforreddit.domain.model.feed.Feed

data class Listing<T>(val items: List<T>, val offset: Int, val reachedTheEnd: Boolean)