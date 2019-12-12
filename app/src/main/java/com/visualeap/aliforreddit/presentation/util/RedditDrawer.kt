package com.visualeap.aliforreddit.presentation.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.children
import com.google.android.material.navigation.NavigationView
import com.visualeap.aliforreddit.R
import kotlinx.android.synthetic.main.drawer_layout.view.*

class RedditDrawer : NavigationView {
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
        inflate(context, R.layout.drawer_layout, this)
        rootLayout.children.forEach { child ->
            if (child is DrawerItem) {
                child.setOnClickListener { navigationItemSelectedListener?.invoke(it) }
            }
        }

        navUsername.setOnClickListener { navigationItemSelectedListener?.invoke(it) }
        navDarkMode.setOnClickListener { navigationItemSelectedListener?.invoke(it) }
    }
}