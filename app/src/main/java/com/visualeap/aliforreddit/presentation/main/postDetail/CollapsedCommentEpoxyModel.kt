package com.visualeap.aliforreddit.presentation.main.postDetail

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginStart
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.model.CommentView
import com.visualeap.aliforreddit.presentation.util.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_collapsed_comment)
abstract class CollapsedCommentEpoxyModel : CommentEpoxyModel<CollapsedCommentHolder>() {
    override fun bind(holder: CollapsedCommentHolder) {
        super.bind(holder)
        holder.commentSummary.text =
            "${comment.authorName} • ${comment.timestamp} • ${comment.text}"
    }
}

class CollapsedCommentHolder : CommentHolder() {
    override var defaultMarginStart: Int = 0
    override lateinit var commentLayoutParams: ConstraintLayout.LayoutParams
    override lateinit var constraintLayout: ConstraintLayout

    val commentSummary by bind<TextView>(R.id.commentSummary)

    override fun bindView(itemView: View) {
        super.bindView(itemView)
        commentLayoutParams = commentSummary.layoutParams as ConstraintLayout.LayoutParams
        defaultMarginStart = commentSummary.marginStart
        constraintLayout = itemView.findViewById(R.id.constraintLayout)
    }
}