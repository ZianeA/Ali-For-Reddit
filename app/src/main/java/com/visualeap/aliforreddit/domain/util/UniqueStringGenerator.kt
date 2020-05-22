package com.visualeap.aliforreddit.domain.util

import dagger.Reusable
import java.math.BigInteger
import java.security.SecureRandom
import javax.inject.Inject

@Reusable
class UniqueStringGenerator @Inject constructor() {
    fun generate(): String = BigInteger(128, SecureRandom()).toString(32)
}