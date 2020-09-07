package com.visualeap.aliforreddit.presentation.common.util

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.annotation.Dimension

fun dpToPx(context: Context, @Dimension(unit = Dimension.DP) dp: Int): Int {
    val r = context.resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        r.displayMetrics
    ).toInt()
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

inline fun <T : View> T.showIf(condition: (T) -> Boolean): T {
    if (condition(this)) {
        show()
    } else {
        hide()
    }

    return this
}

inline fun <T : View> T.hideIf(condition: (T) -> Boolean): T {
    if (condition(this)) {
        hide()
    } else {
        show()
    }

    return this
}