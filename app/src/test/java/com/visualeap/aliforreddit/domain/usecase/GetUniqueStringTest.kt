package com.visualeap.aliforreddit.domain.usecase

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GetUniqueStringTest {

    @Test
    fun `return a unique string`() {
        //Act
        val uniqueString = GetUniqueString().execute(Unit)
        val uniqueString2 = GetUniqueString().execute(Unit)

        //Assert
        assertThat(uniqueString).isNotEmpty().isNotEqualTo(uniqueString2)
    }

    @Test
    fun `return same string for the same instance`() {
        //Arrange
        val getUniqueString = GetUniqueString()

        //Act
        val uniqueString = getUniqueString.execute(Unit)
        val uniqueString2 = getUniqueString.execute(Unit)

        //Assert
        assertThat(uniqueString).isEqualTo(uniqueString2)
    }
}