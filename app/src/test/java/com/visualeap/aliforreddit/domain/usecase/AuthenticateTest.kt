package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.SyncSchedulerProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class AuthenticateTest {

    @Rule
    @JvmField
    val rule: MockitoRule = MockitoJUnit.rule()

    private val authenticateUser = AuthenticateUser(SyncSchedulerProvider())

    @Test
    fun authenticateUser() {
        
    }
}