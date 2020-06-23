package com.visualeap.aliforreddit.presentation.frontPage

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.common.util.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_loading_post)
abstract class LoadingPostEpoxyModel : EpoxyModelWithHolder<LoadingPostHolder>()
class LoadingPostHolder : KotlinEpoxyHolder()