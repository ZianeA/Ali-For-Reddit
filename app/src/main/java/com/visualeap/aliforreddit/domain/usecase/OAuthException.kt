package com.visualeap.aliforreddit.domain.usecase

/** Generic exception to capture all OAuth related issues */
class OAuthException(message: String, cause: Exception? = null) : RuntimeException(message, cause)
