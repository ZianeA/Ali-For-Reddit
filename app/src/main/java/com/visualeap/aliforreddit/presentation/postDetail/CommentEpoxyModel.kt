package com.visualeap.aliforreddit.presentation.postDetail

import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.common.model.CommentDto
import com.visualeap.aliforreddit.presentation.common.util.KotlinEpoxyHolder

abstract class CommentEpoxyModel<T : CommentHolder> : EpoxyModelWithHolder<T>() {
    @EpoxyAttribute
    lateinit var comment: CommentDto

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
                    commentLines[i]!!.visibility = View.VISIBLE
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
            // Unfortunately there's no function for setting bottom margin directly, similar to marginStart
            val bottomMargin = if (comment.isLastReply) defaultMarginStart else 0
            commentLines.forEach { (_, line) ->
                (line.layoutParams as ConstraintLayout.LayoutParams).apply {
                    setMargins(leftMargin, topMargin, rightMargin, bottomMargin)
                }
            }
            view.setOnLongClickListener(longClickListener)
        }
    }

    override fun unbind(holder: T) {
        super.unbind(holder)
        holder.commentLines.forEach { (_, view) -> view.visibility = View.GONE }
        holder.view.setOnLongClickListener(null)
    }
}

abstract class CommentHolder : KotlinEpoxyHolder() {
    lateinit var view: View
    abstract var commentLayoutParams: ConstraintLayout.LayoutParams
    abstract var defaultMarginStart: Int
    abstract var constraintLayout: ConstraintLayout
    lateinit var inflater: LayoutInflater
    val commentLines = mutableMapOf<Int, View>()

    override fun bindView(itemView: View) {
        super.bindView(itemView)
        view = itemView
        inflater = LayoutInflater.from(view.context);
    }
}