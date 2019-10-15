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
import com.visualeap.aliforreddit.presentation.util.KotlinEpoxyHolder
import java.util.concurrent.TimeUnit

@EpoxyModelClass(layout = R.layout.item_post)
abstract class PostEpoxyModel : EpoxyModelWithHolder<PostHolder>() {
    companion object {
        private const val MONTH_IN_MILLIS: Long = WEEK_IN_MILLIS * 4
    }

    @EpoxyAttribute
    lateinit var post: Post

    @EpoxyAttribute
    lateinit var listener: View.OnClickListener

    override fun bind(holder: PostHolder) {
//        holder.imageView.setImageURI(imageUrl)
        holder.apply {
            val subreddit = post.subreddit
            postTitle.text = post.title
            postText.text = post.text
            postedByAndAt.text = "Posted by u/${post.authorName} • ${formatTime(post.created)}"
            postScore.text = post.score.toString()
            postCommentCount.text = post.commentCount.toString()
            subredditName.text = "r/${subreddit.name}"
            view.setOnClickListener(listener)

            //Subreddits' icons have no alpha/transparency.
            subredditImage.background.setColorFilter(
                Color.parseColor(
                    subreddit.primaryColor ?: subreddit.keyColor ?: "#33a8ff"
                ), PorterDuff.Mode.MULTIPLY
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

    //TODO extract this method and unit test it. Maybe make an extension method with a name toTimeStamp
    private fun formatTime(created: Long): String {
        val createdInMillis = TimeUnit.SECONDS.toMillis(created)
        val now = System.currentTimeMillis()
        val timeSpan = now - createdInMillis
        return if (timeSpan < MINUTE_IN_MILLIS)
            "just now"
        else if (timeSpan < HOUR_IN_MILLIS)
            "${timeSpan / MINUTE_IN_MILLIS}m"
        else if (timeSpan < DAY_IN_MILLIS)
            "${timeSpan / HOUR_IN_MILLIS}h"
        else if (timeSpan < MONTH_IN_MILLIS)
            "${timeSpan / DAY_IN_MILLIS}d"
        else if (timeSpan < YEAR_IN_MILLIS)
            "${timeSpan / MONTH_IN_MILLIS}mo"
        else "${timeSpan / YEAR_IN_MILLIS}y"
        /*getRelativeTimeSpanString(createdInMillis, now, 0, FORMAT_ABBREV_ALL).toString()*/
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

