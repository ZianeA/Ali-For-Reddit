package com.visualeap.aliforreddit.presentation.postDetail

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginStart
import com.airbnb.epoxy.EpoxyModelClass
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.common.formatter.formatCount
import com.visualeap.aliforreddit.presentation.common.formatter.formatTimestamp

@EpoxyModelClass(layout = R.layout.item_comment)
abstract class ExpandedCommentEpoxyModel : CommentEpoxyModel<ExpandedCommentHolder>() {
    override fun bind(holder: ExpandedCommentHolder) {
        super.bind(holder)

        holder.commentAuthorAndTime.text =
            "${comment.authorName} • ${formatTimestamp(comment.creationDate)}"
        holder.commentBody.text = comment.text
        holder.commentScore.text = formatCount(comment.score)
    }
}

class ExpandedCommentHolder : CommentHolder() {
    override var defaultMarginStart: Int = 0
    override lateinit var commentLayoutParams: ConstraintLayout.LayoutParams
    override lateinit var constraintLayout: ConstraintLayout

    val commentAuthorAndTime by bind<TextView>(R.id.commentAuthorAndTime)
    val commentBody by bind<TextView>(R.id.commentBody)
    val commentScore by bind<TextView>(R.id.commentScore)

    override fun bindView(itemView: View) {
        super.bindView(itemView)
        commentLayoutParams = commentAuthorAndTime.layoutParams as ConstraintLayout.LayoutParams
        defaultMarginStart = commentAuthorAndTime.marginStart
        constraintLayout = itemView.findViewById(R.id.constraintLayout)
    }
}