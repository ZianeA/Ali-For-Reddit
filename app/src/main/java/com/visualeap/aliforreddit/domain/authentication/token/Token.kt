package com.visualeap.aliforreddit.domain.authentication.token

interface Token {
    val id: Int
    val accessToken: String
    val type: String
}