package com.visualeap.aliforreddit.presentation.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.visualeap.aliforreddit.R
import kotlinx.android.synthetic.main.item_drawer.view.*

class DrawerItem : ConstraintLayout {
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
        var iconDrawable: Drawable?
        var iconTint: Int = 0
        var titleText: CharSequence = ""
        var descriptionText: CharSequence = ""

        context.obtainStyledAttributes(
            attrs,
            R.styleable.DrawerItem,
            defStyleAttr,
            defStyleRes
        ).apply {
            iconDrawable = getDrawable(R.styleable.DrawerItem_item_icon)
            iconTint = getColor(
                R.styleable.DrawerItem_icon_tint,
                ContextCompat.getColor(context, R.color.drawer_icon_color)
            )
            titleText = getText(R.styleable.DrawerItem_item_title)
            descriptionText = getText(R.styleable.DrawerItem_item_description) ?: ""
            recycle()
        }

        inflate(getContext(), R.layout.item_drawer, this)

        val typedArray = context.obtainStyledAttributes(intArrayOf(R.attr.selectableItemBackground))
        val rippleBackground = typedArray.getResourceId(0, 0)
        typedArray.recycle()
        setBackgroundResource(rippleBackground)
        //TODO remove
        setOnClickListener {
            Toast.makeText(
                context,
                "Hello from the other side",
                Toast.LENGTH_SHORT
            ).show()
        }
        val verticalPadding = resources.getDimension(R.dimen.item_drawer_top_bottom_padding).toInt()
        val horizontalPadding =
            resources.getDimension(R.dimen.item_drawer_start_end_padding).toInt()
        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)

        icon.setImageDrawable(iconDrawable)
        ImageViewCompat.setImageTintList(icon, ColorStateList.valueOf(iconTint))
        title.text = titleText
        description.text = descriptionText
        if (descriptionText.isEmpty()) description.visibility = View.GONE
    }
}