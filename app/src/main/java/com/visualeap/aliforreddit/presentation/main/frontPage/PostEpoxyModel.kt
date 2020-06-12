package com.visualeap.aliforreddit.presentation.main.frontPage

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.util.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_post)
abstract class PostEpoxyModel : EpoxyModelWithHolder<PostHolder>() {
    @EpoxyAttribute
    lateinit var post: FeedPostDto

    @EpoxyAttribute
    lateinit var bindListener: () -> Unit

    @EpoxyAttribute
    lateinit var clickListener: View.OnClickListener

    @EpoxyAttribute
    var maxLines: Int = 0

    override fun bind(holder: PostHolder) {
        bindListener()

        holder.postTitle.text = post.title
        holder.postText.maxLines = maxLines
        holder.postText.text = post.text

        if (post.text.isEmpty()) {
            holder.postText.visibility = View.GONE
        }

        holder.postedByAndAt.text = holder.view.context.getString(
            R.string.post_posted_by_and_at,
            post.authorName,
            post.timestamp
        )
        holder.postScore.text = post.score
        holder.postCommentCount.text = post.commentCount
        holder.subredditName.text = post.subreddit
        holder.view.setOnClickListener(clickListener)

        //Subreddits' icons have no alpha/transparency.
        holder.subredditImage.background.setColorFilter(
            Color.parseColor(post.subredditColor),
            PorterDuff.Mode.MULTIPLY
        )

        if (post.subredditIconUrl.isNullOrEmpty()) {
            //If a subreddit has no icon we use a default subreddit icon.
            holder.subredditImage.scaleType = ImageView.ScaleType.CENTER
            holder.subredditImage.setImageDrawable(holder.defaultSubredditIcon)
        } else {
            Glide.with(holder.view)
                .load(post.subredditIconUrl)
                .into(holder.subredditImage);
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
    lateinit var defaultSubredditIcon: Drawable

    override fun bindView(itemView: View) {
        super.bindView(itemView)
        view = itemView
        defaultSubredditIcon =
            ContextCompat.getDrawable(view.context, R.drawable.ic_subreddit_default)!!
    }

    val postTitle by bind<TextView>(R.id.postTitle)
    val postText by bind<TextView>(R.id.postText)
    val postScore by bind<TextView>(R.id.postScore)
    val postCommentCount by bind<TextView>(R.id.postCommentCount)
    val postedByAndAt by bind<TextView>(R.id.postedByAndAt)
    val subredditName by bind<TextView>(R.id.subredditName)
    val subredditImage by bind<ImageView>(R.id.subredditImage)
}

