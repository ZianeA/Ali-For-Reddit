package com.visualeap.aliforreddit.presentation.frontPage

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.common.util.KotlinEpoxyHolder
import com.visualeap.aliforreddit.presentation.common.util.showIf

@EpoxyModelClass(layout = R.layout.item_post)
abstract class PostEpoxyModel : EpoxyModelWithHolder<PostHolder>() {
    @EpoxyAttribute
    lateinit var post: PostDto

    @EpoxyAttribute
    var bindListener: (() -> Unit)? = null

    @EpoxyAttribute
    lateinit var clickListener: View.OnClickListener

    // TODO find a better approach
    @EpoxyAttribute
    var maxLines: Int = 0

    override fun bind(holder: PostHolder) {
        bindListener?.invoke()

        holder.apply {
            postTitle.text = post.title

            postText.maxLines = maxLines
            postText.text = post.text
            postText.showIf { post.text != null }

            postImage.showIf { post.url != null }
            Glide.with(holder.view)
                .load(post.url)
                .into(postImage)

            postedByAndAt.text = view.context.getString(
                R.string.post_posted_by_and_at,
                post.authorName,
                post.timestamp
            )
            postScore.text = post.score
            postCommentCount.text = post.commentCount
            subredditName.text = post.subreddit
            view.setOnClickListener(clickListener)

            subredditImage.background.setColorFilter(
                Color.parseColor(post.subredditColor),
                PorterDuff.Mode.MULTIPLY
            )

            when (val icon = post.subredditIcon) {
                is SubredditIcon.Custom -> {
                    Glide.with(holder.view)
                        .load(icon.url)
                        .into(subredditImage)
                }
                is SubredditIcon.Default -> {
                    subredditImage.scaleType = ImageView.ScaleType.CENTER
                    subredditImage.setImageDrawable(
                        ContextCompat.getDrawable(view.context, icon.resId)
                    )
                }
            }
        }
    }

    override fun unbind(holder: PostHolder) {
        super.unbind(holder)
        holder.postText.visibility = View.VISIBLE
        holder.subredditImage.scaleType = ImageView.ScaleType.FIT_CENTER
    }
}

class PostHolder : KotlinEpoxyHolder() {
    lateinit var view: View

    override fun bindView(itemView: View) {
        super.bindView(itemView)
        view = itemView
    }

    val postTitle by bind<TextView>(R.id.postTitle)
    val postText by bind<TextView>(R.id.postText)
    val postImage by bind<ImageView>(R.id.postImage)
    val postScore by bind<TextView>(R.id.postScore)
    val postCommentCount by bind<TextView>(R.id.postCommentCount)
    val postedByAndAt by bind<TextView>(R.id.postedByAndAt)
    val subredditName by bind<TextView>(R.id.subredditName)
    val subredditImage by bind<ImageView>(R.id.subredditImage)
}

