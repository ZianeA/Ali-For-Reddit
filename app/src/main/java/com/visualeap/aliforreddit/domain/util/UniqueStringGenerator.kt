package com.visualeap.aliforreddit.domain.util

import dagger.Reusable
import java.math.BigInteger
import java.security.SecureRandom
import javax.inject.Inject

object UniqueStringGenerator {
    fun generate(): String = BigInteger(128, SecureRandom()).toString(32)
}