package com.visualeap.aliforreddit

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap

class RoundedImageView : AppCompatImageView {

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
                getDimensionPixelSize(R.styleable.RoundedImageView_cornerRadius, DEFAULT_RADIUS)

            recycle()
        }
    }

    //Get the correct width and height values by waiting until they are calculated.
    // View.post and layout events won't work in preview mode.
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //When the bitmap's height and width are not set to ImageView's, corner radius won't work as expected.
        drawable?.run {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = MeasureSpec.getSize(heightMeasureSpec)
            val roundedDrawable =
                RoundedBitmapDrawableFactory.create(resources, toBitmap(width, height))
            roundedDrawable.cornerRadius = cornerRadius.toFloat()

            setImageDrawable(roundedDrawable)
        }
    }

    companion object {
        private const val DEFAULT_RADIUS = 0
    }
}