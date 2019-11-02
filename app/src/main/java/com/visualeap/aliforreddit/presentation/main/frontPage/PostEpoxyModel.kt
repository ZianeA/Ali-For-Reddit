package com.visualeap.aliforreddit.presentation.main.frontPage

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.text.format.DateUtils.*
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.model.Subreddit
import com.visualeap.aliforreddit.presentation.model.PostView
import com.visualeap.aliforreddit.presentation.util.KotlinEpoxyHolder
import com.visualeap.aliforreddit.presentation.util.formatTimestamp
import java.util.concurrent.TimeUnit

@EpoxyModelClass(layout = R.layout.item_post)
abstract class PostEpoxyModel : EpoxyModelWithHolder<PostHolder>() {
    @EpoxyAttribute
    lateinit var post: PostView

    @EpoxyAttribute
    lateinit var listener: View.OnClickListener

    override fun bind(holder: PostHolder) {
        holder.apply {
            val subreddit = post.subreddit
            postTitle.text = post.title
            postText.text = post.text
            postedByAndAt.text = view.context.getString(R.string.post_posted_by_and_at, post.authorName, post.timestamp)
            postScore.text = post.score
            postCommentCount.text = post.commentCount
            subredditName.text = subreddit.name
            view.setOnClickListener(listener)

            //Subreddits' icons have no alpha/transparency.
            subredditImage.background.setColorFilter(
                Color.parseColor(subreddit.color),
                PorterDuff.Mode.MULTIPLY
            )

            if (subreddit.iconUrl.isNullOrEmpty()) {
                //If a subreddit has no icon we use a default subreddit icon.
                subredditImage.scaleType = ImageView.ScaleType.CENTER
                val defaultSubredditIcon =
                    ContextCompat.getDrawable(view.context, R.drawable.ic_subreddit_default)
                subredditImage.setImageDrawable(defaultSubredditIcon)
            } else {
                subredditImage.scaleType = ImageView.ScaleType.FIT_CENTER

                Glide.with(view)
                    .load(subreddit.iconUrl)
                    .into(subredditImage);
            }
        }
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
    val postScore by bind<TextView>(R.id.postScore)
    val postCommentCount by bind<TextView>(R.id.postCommentCount)
    val postedByAndAt by bind<TextView>(R.id.postedByAndAt)
    val subredditName by bind<TextView>(R.id.subredditName)
    val subredditImage by bind<ImageView>(R.id.subredditImage)
}

