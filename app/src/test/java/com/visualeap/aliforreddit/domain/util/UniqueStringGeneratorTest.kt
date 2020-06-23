package com.visualeap.aliforreddit.domain.util

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UniqueStringGeneratorTest {
    @Test
    fun `should return a unique string every time`() {
        //Arrange
        val values = mutableListOf<String>()

        //Act
        repeat(10) {
            values.add(UniqueStringGenerator.generate())
        }

        //Assert
        Assertions.assertThat(values).doesNotHaveDuplicates()
    }
}