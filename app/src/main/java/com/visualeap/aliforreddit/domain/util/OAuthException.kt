package com.visualeap.aliforreddit.domain.util

/** Generic exception to capture OAuth related issues */
class OAuthException(message: String, cause: Exception? = null) : RuntimeException(message, cause)
