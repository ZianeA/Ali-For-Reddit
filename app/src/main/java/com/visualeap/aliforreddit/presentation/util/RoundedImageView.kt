package com.visualeap.aliforreddit.presentation.util

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import com.visualeap.aliforreddit.R


class RoundedImageView : AppCompatImageView {
    private var isCircular = false
    private var cornerRadius = 0

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
        context.obtainStyledAttributes(
            attrs,
            R.styleable.RoundedImageView,
            defStyleAttr,
            defStyleRes
        ).apply {
            cornerRadius =
                getDimensionPixelSize(
                    R.styleable.RoundedImageView_cornerRadius,
                    DEFAULT_RADIUS
                )
            isCircular = getBoolean(R.styleable.RoundedImageView_isCircular, true)

            recycle()
        }

        //Request another setImageDrawable because isCircular and cornerRadius were not set yet when this method was initially called
        setImageDrawable(drawable)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        if (drawable != null && drawable.intrinsicWidth > 0) {
            val roundedDrawable =
                RoundedBitmapDrawableFactory.create(
                    resources,
                    drawable.toBitmap()
                )
            roundedDrawable.cornerRadius = cornerRadius.toFloat()
            roundedDrawable.isCircular = isCircular
            super.setImageDrawable(roundedDrawable)
        } else {
            super.setImageDrawable(drawable)
        }
    }

    companion object {
        private const val DEFAULT_RADIUS = 0
    }
}