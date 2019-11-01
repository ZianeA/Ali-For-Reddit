package com.visualeap.aliforreddit.data.repository.token

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createTokenWithUserlessTokenEntity
import util.domain.createUserlessToken

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TokenWithUserlessTokenEntityMapperTest{
    private val mapper = TokenWithUserlessTokenEntityMapper()

    @Test
    fun `map TokenWithUserlessTokenEntity to UserlessToken`() {
        //Act
        val mappedUserlessToken = mapper.map(createTokenWithUserlessTokenEntity())

        //Assert
        assert(mappedUserlessToken == createUserlessToken())
    }

    @Test
    fun `map UserlessToken to TokenWithUserlessTokenEntity`() {
        //Act
        val mappedTokenWithUserlessToken = mapper.mapReverse(createUserlessToken())

        //Assert
        assert(mappedTokenWithUserlessToken == createTokenWithUserlessTokenEntity())
    }
}