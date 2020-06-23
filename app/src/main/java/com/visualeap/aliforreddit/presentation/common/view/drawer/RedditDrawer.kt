package com.visualeap.aliforreddit.presentation.common.view.drawer

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.redditor.Redditor
import com.visualeap.aliforreddit.presentation.common.formatter.formatTimestamp
import kotlinx.android.synthetic.main.drawer_online.view.*
import kotlinx.android.synthetic.main.drawer_item.view.*

class RedditDrawer : NavigationView {
    private var layoutOnline: ViewGroup? = null
    private var layoutOffline: ViewGroup? = null

    var navigationItemSelectedListener: ((selectedItem: View) -> Unit)? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {}

    fun showRedditorInfo(redditor: Redditor) {
        layoutOffline?.visibility = View.GONE

        if (layoutOnline == null) {
            layoutOnline = inflate(context, R.layout.drawer_online, null) as ViewGroup
            layoutOnline?.let {
                addView(it)
                setupClickListeners(it)
                it.navUsername
                    .setOnClickListener { v -> navigationItemSelectedListener?.invoke(v) }
            }
        }

        layoutOnline?.let {
            it.visibility = View.VISIBLE
            it.username.text = redditor.username
            it.karma.text = "${redditor.linkKarma + redditor.commentKarma}"
            it.redditAge.text =
                formatTimestamp(
                    redditor.creationDate
                )
            it.navCoins.description.text = "${redditor.coins} Coins"
            Glide.with(this)
                .load(redditor.iconUrl)
                .placeholder(R.drawable.default_profile_icon)
                .into(it.profileImage)
        }
    }

    fun showOfflineMode() {
        layoutOnline?.visibility = View.GONE

        if (layoutOffline == null) {
            layoutOffline = inflate(context, R.layout.drawer_offline, null) as ViewGroup
            layoutOffline?.let {
                it.visibility = View.VISIBLE
                addView(it)
                setupClickListeners(it)
            }
        }
    }

    private fun setupClickListeners(layout: ViewGroup) {
        layout.children.forEach { child ->
            if (child is DrawerItem) {
                child.setOnClickListener { navigationItemSelectedListener?.invoke(it) }
            }
        }

        layout.navDarkMode
            .setOnClickListener { v -> navigationItemSelectedListener?.invoke(v) }
    }
}