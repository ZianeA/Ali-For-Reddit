package com.visualeap.aliforreddit.presentation.login

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.visualeap.aliforreddit.R
import kotlinx.android.synthetic.main.fragment_login.*
import android.os.AsyncTask.execute
import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient

//TODO remove this screen (fragment, activity, presenter), and just add the WebView to MainActivity
/**
 * A placeholder fragment containing a simple view.
 */
class LoginFragment : Fragment(), LoginView {

    private lateinit var presenter: LoginPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter =
            LoginPresenter(this, getString(R.string.client_id), getString(R.string.redirect_url))
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun showLoginPage(authUrl: String) {
        // Watch for pages loading
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                presenter.onPageStarted(url)
//                if (helper.isFinalRedirectUrl(url)) {
//                    // No need to continue loading, we've already got all the required information
//                    webView.stopLoading()
//                    webView.visibility = View.GONE
//
//                    // Try to authenticate the user
//
//                }
            }
        }

        webView.loadUrl(authUrl)
    }

    override fun hideLoginPage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
