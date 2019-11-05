/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.visualeap.aliforreddit.domain.repository

import androidx.paging.PagedList
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable

/**
 * Data class that is necessary for a UI to show a listing and interact w/ the rest of the system
 */
data class Listing<T>(
    // the LiveData of paged lists for the UI to observe
    val pagedList: Observable<PagedList<T>>,
    // represents the network request status to show to the user
    val networkState: BehaviorRelay<NetworkState>,
    // retries any failed requests.
    val retry: () -> Unit)