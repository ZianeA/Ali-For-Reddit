package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.entity.Token
import com.visualeap.aliforreddit.domain.usecase.base.NonReactiveUseCase
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetToken @Inject constructor() : NonReactiveUseCase<Token, Unit> {

    override fun execute(params: Unit): Token {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}