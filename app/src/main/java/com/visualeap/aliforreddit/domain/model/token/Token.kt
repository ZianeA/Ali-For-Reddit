package com.visualeap.aliforreddit.domain.model.token

interface Token {
    val id: Int
    val accessToken: String
    val type: String
}