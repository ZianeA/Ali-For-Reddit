package com.visualeap.aliforreddit.presentation.common.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
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
//        setImageDrawable(drawable)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        if (drawable != null && drawable.intrinsicWidth > 0) {
            // When the bitmap's height and width are not the same as the ImageView's height and width, corner radius won't work as expected.
            // Bitmap and ImageView should have the same size only when the scale type is set to FIT_CENTER
            // We have to wait for size to be calculated. If we attempt to Create a bitmap with a size of 0, an exception will be thrown
            val bitmap =
                if (width > 0 && height > 0 && scaleType == ScaleType.FIT_CENTER)
                    drawable.toBitmap(width, height)
                else drawable.toBitmap()

            val roundedDrawable =
                RoundedBitmapDrawableFactory.create(resources, bitmap)
            roundedDrawable.cornerRadius = cornerRadius.toFloat()

            if (isCircular) {
                roundedDrawable.isCircular = isCircular
            }

            super.setImageDrawable(roundedDrawable)
        } else {
            super.setImageDrawable(drawable)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //When the size changes we need to request another setImageDrawable in order for corner radius to work.
        setImageDrawable(drawable)
    }

    companion object {
        private const val DEFAULT_RADIUS = 0
    }
}