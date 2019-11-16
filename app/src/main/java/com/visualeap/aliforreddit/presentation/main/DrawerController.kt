package com.visualeap.aliforreddit.presentation.main

interface DrawerController {
    fun open()
    fun close()
    fun lockClosed()
    fun toggle()
}