package com.visualeap.aliforreddit.presentation.main.postDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginStart
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
            val indent = comment.depth + 1
            commentLayoutParams.marginStart = defaultMarginStart * indent

            commentLines.forEach { (_, view) -> view.visibility = View.INVISIBLE }
            for (i in 1 until indent) {
                if (commentLines.containsKey(i)) {
                    commentLines[i]!!.visibility = View.VISIBLE
                } else {
                    val line = inflater.inflate(R.layout.comment_line, constraintLayout, false)
                    val lineLayoutParams = line.layoutParams as ConstraintLayout.LayoutParams
                    lineLayoutParams.marginStart = defaultMarginStart * i
                    commentLines[i] = line
                    constraintLayout.addView(line)
                }
            }

            commentBody.text = comment.text
        }
    }
}

class CommentHolder : KotlinEpoxyHolder() {
    lateinit var view: View
    lateinit var commentLayoutParams: ConstraintLayout.LayoutParams
    var defaultMarginStart: Int = 0
    val commentLines = mutableMapOf<Int, View>()
    lateinit var inflater: LayoutInflater

    override fun bindView(itemView: View) {
        super.bindView(itemView)
        view = itemView
        commentLayoutParams = commentAuthorAndTime.layoutParams as ConstraintLayout.LayoutParams
        defaultMarginStart = commentAuthorAndTime.marginStart
        inflater = LayoutInflater.from(view.context);
    }

    val commentAuthorAndTime by bind<TextView>(R.id.commentAuthorAndTime)
    val commentBody by bind<TextView>(R.id.commentBody)
    val constraintLayout by bind<ConstraintLayout>(R.id.constraintLayout)
}