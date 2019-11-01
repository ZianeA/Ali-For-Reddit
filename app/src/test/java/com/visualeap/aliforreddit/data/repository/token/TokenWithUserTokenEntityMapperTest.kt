package com.visualeap.aliforreddit.data.repository.token

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createTokenWithUserTokenEntity
import util.domain.createUserToken

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TokenWithUserTokenEntityMapperTest{
    private val mapper = TokenWithUserTokenEntityMapper()

    @Test
    fun `map TokenWithUserTokenEntity to UserToken`() {
        //Act
        val mappedUserToken = mapper.map(createTokenWithUserTokenEntity())

        //Assert
        assert(mappedUserToken == createUserToken())
    }

    @Test
    fun `map UserToken to TokenWithUserTokenEntity`() {
        //Act
        val mappedTokenWithUserTokenEntity = mapper.mapReverse(createUserToken())

        //Assert
        assert(mappedTokenWithUserTokenEntity == createTokenWithUserTokenEntity())
    }
}