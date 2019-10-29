package com.visualeap.aliforreddit.util

import androidx.paging.PagedList
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

fun <T> mockPagedList(list: List<T>): PagedList<T> {
    val pagedList: PagedList<T> = mockk()
    val indexSlot = slot<Int>()
    every { pagedList[capture(indexSlot)] } answers { list[indexSlot.captured] }
    every { pagedList.size } returns list.size
    return pagedList
}
