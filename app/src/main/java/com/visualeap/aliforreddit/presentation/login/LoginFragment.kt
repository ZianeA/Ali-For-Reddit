package com.visualeap.aliforreddit.presentation.login

import android.content.Context
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.visualeap.aliforreddit.R
import android.graphics.Bitmap
import android.os.Build
import android.view.ViewAnimationUtils
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.ncapdevi.fragnav.FragNavController
import com.visualeap.aliforreddit.presentation.common.main.MainActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import javax.inject.Inject
import kotlin.math.hypot

class LoginFragment : Fragment(), LoginView, BackButtonHandler {
    @Inject
    lateinit var presenter: LoginPresenter

    @Inject
    lateinit var fragNavController: FragNavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)
        rootView.loginButton.setOnClickListener { presenter.onLogInClicked() }
        return rootView
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun hideLoginPrompt() {
        loginButton.visibility = View.INVISIBLE
        loginMessage.visibility = View.INVISIBLE
    }

    override fun showLoginPage(authUrl: String) {
        // Watch for pages loading
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                url?.let { presenter.onPageStarted(it) }
            }
        }

        webView.loadUrl(authUrl)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            circularReveal()
        } else {
            webView.visibility = View.VISIBLE
        }
    }

    override fun hideLoginPage() {
        // No need to continue loading, we've already got all the required information
        webView.stopLoading()
        webView.visibility = View.INVISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun circularReveal() {
        //get the center for the clipping circle relative to view.
        val cx = webView.width / 2
        val cy = webView.height / 2

        // get the final radius for the clipping circle
        val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()

        // create the animator for this view (the start radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(webView, cx, cy, 0f, finalRadius)

        // Set background color programmatically. Couldn't set it through XML.
        webView.setBackgroundColor(
            ResourcesCompat.getColor(
                resources,
                R.color.redditLoginPageGray,
                null
            )
        )
        webView.visibility = View.VISIBLE
        // start the animation
        anim.start()
    }

    override fun reloadScreen() {
        //TODO find a better way to handle reload
        (activity as MainActivity).reload()
    }

    override fun onBackPressed(): Boolean {
        return if (webView.visibility == View.VISIBLE) {
            hideLoginPage()
            showLoginPrompt()
            true
        } else {
            false
        }
    }

    private fun showLoginPrompt() {
        loginButton.visibility = View.VISIBLE
        loginMessage.visibility = View.VISIBLE
    }
}
