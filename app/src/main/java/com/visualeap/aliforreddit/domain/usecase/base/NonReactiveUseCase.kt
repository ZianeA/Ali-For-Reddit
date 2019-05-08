package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.core.util.applySchedulers

interface NonReactiveUseCase<T, P> {

    fun execute(param: P): T
}