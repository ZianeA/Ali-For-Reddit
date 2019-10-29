package com.visualeap.aliforreddit.presentation.main.postDetail

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.model.Comment
import com.visualeap.aliforreddit.presentation.util.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_comment)
abstract class CommentEpoxyModel : EpoxyModelWithHolder<CommentHolder>() {
    @EpoxyAttribute
    lateinit var comment: Comment

    override fun bind(holder: CommentHolder) {
        super.bind(holder)

        holder.apply {
            commentAuthorAndTime.text = comment.authorName
            val layoutParams = view.layoutParams as RecyclerView.LayoutParams
            layoutParams.marginStart = commentAuthorAndTime.marginStart * comment.depth
            commentBody.text = comment.text
        }
    }
}

class CommentHolder : KotlinEpoxyHolder() {
    lateinit var view: View

    override fun bindView(itemView: View) {
        super.bindView(itemView)
        view = itemView
    }

    val commentAuthorAndTime by bind<TextView>(R.id.commentAuthorAndTime)
    val commentBody by bind<TextView>(R.id.commentBody)
}