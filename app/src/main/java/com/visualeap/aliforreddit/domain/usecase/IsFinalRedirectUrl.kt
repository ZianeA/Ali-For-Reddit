package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.usecase.base.NonReactiveUseCase
import com.visualeap.aliforreddit.domain.usecase.IsFinalRedirectUrl.*
import dagger.Reusable
import okhttp3.HttpUrl
import javax.inject.Inject
import javax.inject.Named

@Reusable
class IsFinalRedirectUrl @Inject constructor(@Named("redirectUrl") private val redirectUrl: String) :
    NonReactiveUseCase<Boolean, String> {

    /**
     * @param params the final redirect URL
     */
    override fun execute(params: String): Boolean {

            HttpUrl.parse(params)
                ?.let {
                    return if (it.query() != null) params.contains(redirectUrl) else false
                }
                ?: return false
    }
}