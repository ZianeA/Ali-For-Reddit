package com.visualeap.aliforreddit.presentation.main.postDetail

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.model.CommentView
import com.visualeap.aliforreddit.presentation.util.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_collapsed_comment)
abstract class CollapsedCommentEpoxyModel : EpoxyModelWithHolder<CollapsedCommentHolder>() {
    @EpoxyAttribute
    lateinit var comment: CommentView

    override fun bind(holder: CollapsedCommentHolder) {
        super.bind(holder)
        holder.commentSummary.text =
            "${comment.authorName} • ${comment.timestamp} • ${comment.text}"
    }
}

class CollapsedCommentHolder : KotlinEpoxyHolder() {
    val commentSummary by bind<TextView>(R.id.commentSummary)
}