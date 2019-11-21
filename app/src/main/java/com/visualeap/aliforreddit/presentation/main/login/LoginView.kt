package com.visualeap.aliforreddit.presentation.main.login

interface LoginView {
    fun showLoginPage(authUrl: String)
    fun hideLoginPage()
    fun hideLoginPrompt()
}
