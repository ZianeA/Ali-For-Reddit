package com.visualeap.aliforreddit.domain.usecase.base

interface NonReactiveUseCase<Results, Params> {
    fun execute(params: Params): Results
}