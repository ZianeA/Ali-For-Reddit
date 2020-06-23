package com.visualeap.aliforreddit.presentation.postDetail

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyViewHolder

class CommentItemDecoration(private val itemSpacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val commentEpoxyModel =
            (parent.getChildViewHolder(view) as EpoxyViewHolder).model as? CommentEpoxyModel // A single RecyclerView could host multiple item types
        // Comment spacing should only be between root comments
        if (commentEpoxyModel?.comment != null && commentEpoxyModel.comment.depth > 0)
            return

        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = itemSpacing
        }
    }
}