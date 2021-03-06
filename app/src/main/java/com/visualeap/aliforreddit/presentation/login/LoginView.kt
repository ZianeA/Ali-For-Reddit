package com.visualeap.aliforreddit.presentation.login

interface LoginView {
    fun showLoginPage(authUrl: String)
    fun hideLoginPage()
    fun hideLoginPrompt()
    fun reloadScreen()
}
