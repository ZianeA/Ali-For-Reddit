package com.visualeap.aliforreddit.presentation.main.frontPage

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.util.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_loading_post)
abstract class LoadingPostEpoxyModel : EpoxyModelWithHolder<LoadingPostHolder>()
class LoadingPostHolder : KotlinEpoxyHolder()