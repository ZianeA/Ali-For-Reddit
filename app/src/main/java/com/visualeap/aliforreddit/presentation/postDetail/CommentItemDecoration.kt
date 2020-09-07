package com.visualeap.aliforreddit.presentation.postDetail

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyModelGroup
import com.airbnb.epoxy.EpoxyViewHolder

class CommentItemDecoration(private val itemSpacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if (addSpacing(parent, view)) outRect.top = itemSpacing
    }

    private fun addSpacing(parent: RecyclerView, view: View): Boolean {
        val viewHolder = parent.getChildViewHolder(view)

        return if (viewHolder is EpoxyViewHolder) {
            val model = viewHolder.model
            // Spacing for comments only
            model is CommentEpoxyModel
                    // Spacing between root comments only
                    && model.comment.depth == 0
                    // Skip first comment
                    && parent.getChildAdapterPosition(view) > 0
        } else false
    }
}