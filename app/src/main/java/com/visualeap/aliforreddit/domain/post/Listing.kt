package com.visualeap.aliforreddit.domain.post

data class Listing<T>(val items: List<T>, val offset: Int, val reachedTheEnd: Boolean)