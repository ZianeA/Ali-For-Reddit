package com.visualeap.aliforreddit.data.redditor

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createRedditor
import util.domain.createRedditorReponse

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class RedditorResponseMapperTest{
    private val mapper = RedditorResponseMapper()

    @Test
    fun `map RedditorResponse to Redditor`() {
        //Act
        val mappedRedditor = mapper.map(createRedditorReponse())

        //Assert
        assertThat(mappedRedditor).isEqualTo(createRedditor())
    }
}