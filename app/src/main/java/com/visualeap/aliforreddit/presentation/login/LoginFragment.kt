package com.visualeap.aliforreddit.presentation.login

import android.content.Context
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.visualeap.aliforreddit.R
import android.os.AsyncTask.execute
import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import com.visualeap.aliforreddit.core.di.ActivityScope
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

//TODO remove this screen (fragment, activity, presenter), and just add the WebView to MainActivity
/**
 * A placeholder fragment containing a simple view.
 */
class LoginFragment : Fragment(), LoginView {

    @Inject
    lateinit var presenter: LoginPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun showLoginPage(authUrl: String) {
        // Watch for pages loading
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                url?.let { presenter.onPageStarted(it) }
            }
        }

        webView.loadUrl(authUrl)
    }

    override fun hideLoginPage() {
        // No need to continue loading, we've already got all the required information
        webView.stopLoading()
        webView.visibility = View.GONE
    }
}
