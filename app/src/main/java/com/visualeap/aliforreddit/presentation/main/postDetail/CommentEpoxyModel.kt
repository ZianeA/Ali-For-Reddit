package com.visualeap.aliforreddit.presentation.main.postDetail

import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.model.CommentView
import com.visualeap.aliforreddit.presentation.util.KotlinEpoxyHolder

abstract class CommentEpoxyModel<T : CommentHolder> : EpoxyModelWithHolder<T>() {
    @EpoxyAttribute
    lateinit var comment: CommentView

    @EpoxyAttribute
    lateinit var longClickListener: View.OnLongClickListener

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
                    lineLayoutParams.marginStart = holder.defaultMarginStart * i
                    line.id = View.generateViewId()
                    constraintLayout.addView(line)
                    commentLines[i] = line
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