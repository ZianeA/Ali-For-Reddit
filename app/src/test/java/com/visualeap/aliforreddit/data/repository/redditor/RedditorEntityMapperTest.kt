package com.visualeap.aliforreddit.data.repository.redditor

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createRedditor
import util.domain.createRedditorEntity

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class RedditorEntityMapperTest {
    private val mapper = RedditorEntityMapper()

    @Test
    internal fun `map RedditorEntity to Redditor`() {
        //Act
        val mappedRedditor = mapper.map(createRedditorEntity())

        //Assert
        assertThat(mappedRedditor).isEqualTo(createRedditor())
    }

    @Test
    internal fun `map Redditor to RedditorEntity`() {
        //Act
        val mappedRedditorEntity = mapper.mapReverse(createRedditor())

        //Assert
        assertThat(mappedRedditorEntity).isEqualTo(createRedditorEntity())
    }
}