package com.visualeap.aliforreddit.presentation.common.util

import com.visualeap.aliforreddit.presentation.common.formatter.formatCount
import com.visualeap.aliforreddit.presentation.common.formatter.formatTimestamp
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FormatUtilKtTest {

    companion object {
        private const val SECOND_IN_MILLIS: Long = 1000
    }

    @Nested
    inner class FormatTimestamp{
        @Test
        fun `handle seconds`() {
            //Arrange
            val aSecondAgo = currentTimeInSeconds - 1

            //Act
            val timestamp =
                formatTimestamp(
                    aSecondAgo
                )

            //Assert
            assertThat(timestamp).isEqualTo("just now")
        }

        @Test
        fun `handle minutes`() {
            //Arrange
            val aMinuteAgo = currentTimeInSeconds - 60

            //Act
            val timestamp =
                formatTimestamp(
                    aMinuteAgo
                )

            //Assert
            assertThat(timestamp).isEqualTo("1m")
        }

        @Test
        fun `handle hours`() {
            //Arrange
            val anHourAgo = currentTimeInSeconds - 3600

            //Act
            val timestamp =
                formatTimestamp(
                    anHourAgo
                )

            //Assert
            assertThat(timestamp).isEqualTo("1h")
        }

        @Test
        fun `handle days`() {
            //Arrange
            val yesterday = currentTimeInSeconds - 3600 * 24

            //Act
            val timestamp =
                formatTimestamp(
                    yesterday
                )

            //Assert
            assertThat(timestamp).isEqualTo("1d")
        }

        @Test
        fun `handle months`() {
            //Arrange
            val aMonthAgo = currentTimeInSeconds - 3600 * 24 * 30

            //Act
            val timestamp =
                formatTimestamp(
                    aMonthAgo
                )

            //Assert
            assertThat(timestamp).isEqualTo("1mo")
        }

        @Test
        fun `handle years`() {
            //Arrange
            val aYearAgo = currentTimeInSeconds - 3600 * 24 * 30 * 12

            //Act
            val timestamp =
                formatTimestamp(
                    aYearAgo
                )

            //Assert
            assertThat(timestamp).isEqualTo("1y")
        }

        @Test
        fun `handle wrongly formatted dates`() {
            //Act
            val timestamp =
                formatTimestamp(
                    -1
                )

            //Assert
            assertThat(timestamp).isEqualTo("unknown")
        }
    }

    @Nested
    inner class FormatCount{
        @Test
        fun `handle number under a thousand`() {
            //Act
            val count =
                formatCount(
                    999
                )

            //Assert
            assertThat(count).isEqualTo("999")
        }

        @Test
        fun `handle number over a thousand`() {
            //Act
            val count =
                formatCount(
                    1021
                )

            //Arrange
            assertThat(count).isEqualTo("1.0k")
        }

        @Test
        fun `handle number over ten thousand`() {
            //Act
            val count =
                formatCount(
                    45321
                )

            //Arrange
            assertThat(count).isEqualTo("45.3k")
        }
    }

    private val currentTimeInSeconds
        get() = System.currentTimeMillis() / SECOND_IN_MILLIS
}