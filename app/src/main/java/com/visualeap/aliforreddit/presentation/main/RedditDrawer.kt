package com.visualeap.aliforreddit.presentation.main

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.model.Redditor
import com.visualeap.aliforreddit.presentation.util.formatTimestamp
import kotlinx.android.synthetic.main.drawer_online.view.*
import kotlinx.android.synthetic.main.drawer_item.view.*

class RedditDrawer : NavigationView {
    private var layoutOnline: View? = null
    private var layoutOffline: View? = null

    var navigationItemSelectedListener: ((selectedItem: View) -> Unit)? = null

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int = 0
    ) {

    }

    fun showRedditorInfo(redditor: Redditor) {
        layoutOffline?.visibility = View.GONE

        if (layoutOnline == null) {
            layoutOnline = inflate(context, R.layout.drawer_online, null)
            layoutOnline?.let {
                addView(it)
                setupClickListeners()
                it.navDarkMode
                    .setOnClickListener { v -> navigationItemSelectedListener?.invoke(v) }
                it.navUsername
                    .setOnClickListener { v -> navigationItemSelectedListener?.invoke(v) }
            }
        }

        layoutOnline?.let {
            it.visibility = View.VISIBLE
            it.username.text = redditor.username
            it.karma.text = "${redditor.linkKarma + redditor.commentKarma}"
            it.redditAge.text =
                formatTimestamp(redditor.creationDate)
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
            layoutOffline = inflate(context, R.layout.drawer_offline, null)
            layoutOffline?.let {
                it.visibility = View.VISIBLE
                addView(it)
                setupClickListeners()
                it.navDarkMode
                    .setOnClickListener { v -> navigationItemSelectedListener?.invoke(v) }
            }
        }
    }

    private fun setupClickListeners() {
        children.forEach { child ->
            if (child is DrawerItem) {
                child.setOnClickListener { navigationItemSelectedListener?.invoke(it) }
            }
        }
    }
}