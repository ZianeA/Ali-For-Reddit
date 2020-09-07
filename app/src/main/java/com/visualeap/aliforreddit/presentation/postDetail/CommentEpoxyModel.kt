package com.visualeap.aliforreddit.presentation.postDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateMargins
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.comment.Comment
import com.visualeap.aliforreddit.presentation.common.util.KotlinEpoxyHolder
import com.visualeap.aliforreddit.presentation.common.util.dpToPx
import com.visualeap.aliforreddit.presentation.common.util.hide
import com.visualeap.aliforreddit.presentation.common.util.show

abstract class CommentEpoxyModel<T : CommentHolder> : EpoxyModelWithHolder<T>() {
    @EpoxyAttribute
    lateinit var comment: Comment

    @JvmField
    @EpoxyAttribute
    var isLastReplay = false

    @EpoxyAttribute
    var longClickListener: View.OnLongClickListener? = null

    override fun bind(holder: T) {
        super.bind(holder)
        holder.apply {
            val indent = comment.depth + 1
            commentLayoutParams.marginStart = defaultMarginStart * indent

            //Lazily create comment tree lines.
            for (i in 1 until indent) {
                if (commentLines.containsKey(i)) {
                    commentLines[i]!!.show()
                } else {
                    val line =
                        inflater.inflate(R.layout.comment_line, constraintLayout, false)
                    val lineLayoutParams = line.layoutParams as ConstraintLayout.LayoutParams
                    lineLayoutParams.marginStart = defaultMarginStart * i
                    line.id = View.generateViewId()
                    constraintLayout.addView(line)
                    commentLines[i] = line
                }
            }

            // Add bottom margin at the end of comment tree line
            val bottomMargin = if (isLastReplay) dpToPx(context, 8) else 0
            commentLines.forEach { (_, line) ->
                (line.layoutParams as ConstraintLayout.LayoutParams).updateMargins(bottom = bottomMargin)
            }
            view.setOnLongClickListener(longClickListener)
        }
    }

    override fun unbind(holder: T) {
        super.unbind(holder)
        holder.commentLines.forEach { (_, view) -> view.hide() }
        holder.view.setOnLongClickListener(null)
    }
}

abstract class CommentHolder : KotlinEpoxyHolder() {
    lateinit var view: View
    lateinit var context: Context
    abstract var commentLayoutParams: ConstraintLayout.LayoutParams
    abstract var defaultMarginStart: Int
    abstract var constraintLayout: ConstraintLayout
    lateinit var inflater: LayoutInflater
    val commentLines = mutableMapOf<Int, View>()

    override fun bindView(itemView: View) {
        super.bindView(itemView)
        context = itemView.context
        view = itemView
        inflater = LayoutInflater.from(context);
    }
}