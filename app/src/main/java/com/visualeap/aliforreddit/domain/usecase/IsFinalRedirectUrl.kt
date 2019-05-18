package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.usecase.base.NonReactiveUseCase
import com.visualeap.aliforreddit.domain.usecase.IsFinalRedirectUrl.*
import dagger.Reusable
import okhttp3.HttpUrl
import javax.inject.Inject

@Reusable
class IsFinalRedirectUrl @Inject constructor() : NonReactiveUseCase<Boolean, Params> {

    override fun execute(params: Params): Boolean {
        params.run {
            HttpUrl.parse(finalRedirectUrl)
                ?.let { it.query() ?: return false }
                ?: return false

            return finalRedirectUrl.contains(redirectUrl)
        }
    }

    data class Params(val redirectUrl: String, val finalRedirectUrl: String)
}