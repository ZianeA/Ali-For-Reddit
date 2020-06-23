package com.visualeap.aliforreddit.presentation.common.view.drawer

interface DrawerController {
    fun open()
    fun close()
    fun lockClosed()
    fun toggle()
}