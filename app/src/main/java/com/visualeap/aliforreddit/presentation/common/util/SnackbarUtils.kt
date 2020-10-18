package com.visualeap.aliforreddit.presentation.common.util

import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.updatePadding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.BaseTransientBottomBar.*
import com.google.android.material.snackbar.Snackbar


/*
snackBar.view.fitsSystemWindows = false
snackBar.view.setOnApplyWindowInsetsListener { v, insets ->
    v.updatePadding(bottom = 0)
    insets
}*/

/**
 * Create snackbar without padding from window insets.
 */
fun View.makeSnackbar(@StringRes text: Int, @Duration duration: Int = Snackbar.LENGTH_LONG) =
    makeSnackbar(resources.getString(text))

fun View.makeSnackbar(text: String, @Duration duration: Int = Snackbar.LENGTH_LONG): Snackbar {
    val snackbar = Snackbar.make(this, text, duration)
    snackbar.view.fitsSystemWindows = false
    snackbar.view.setOnApplyWindowInsetsListener { v, insets ->
        v.updatePadding(bottom = 0)
        insets
    }

    return snackbar
}