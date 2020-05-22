package com.visualeap.aliforreddit.domain.util

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.common.ResourceProvider
import dagger.Reusable
import okhttp3.Credentials
import javax.inject.Inject

@Reusable
class BasicAuthCredentialProvider @Inject constructor(private val resourceProvider: ResourceProvider) {
    fun getAuthCredential(): String =
        Credentials.basic(resourceProvider.getString(R.string.client_id), "")
}