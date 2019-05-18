package com.visualeap.aliforreddit.presentation.login

import android.util.Log
import com.visualeap.aliforreddit.presentation.di.FragmentScope
import com.visualeap.aliforreddit.domain.entity.Credentials
import com.visualeap.aliforreddit.domain.usecase.*
import javax.inject.Inject

@FragmentScope
class LoginPresenter @Inject constructor(
    private val view: LoginView,
    private val credentials: Credentials,
    private val getUniqueString: GetUniqueString,
    private val getAuthUrl: GetAuthUrl,
    private val isFinalRedirectUrl: IsFinalRedirectUrl,
    private val authenticateUser: AuthenticateUser,
    private val authService: AuthService
) {

    private val tag = LoginPresenter::class.java.simpleName

    fun start() {
        val authUrl = getAuthUrl.execute(
            GetAuthUrl.Params(
                credentials,
                getUniqueString.execute(Unit)
            )
        )

        view.showLoginPage(authUrl)
    }

    fun onPageStarted(url: String) {
        if (isFinalRedirectUrl.execute(IsFinalRedirectUrl.Params(credentials.redirectUrl, url))) {
            view.hideLoginPage()

            //Authenticate user
            val params = AuthenticateUser.Params(
                authService,
                url,
                credentials,
                getUniqueString.execute(Unit)
            )

            val disposable = authenticateUser.execute(params).subscribe({ /*TODO on sucess*/ },
                { /*TODO error*/ Log.i(tag, it.message) })
        }
    }
}