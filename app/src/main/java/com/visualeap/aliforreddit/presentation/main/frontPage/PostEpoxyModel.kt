package com.visualeap.aliforreddit.presentation.main.frontPage

import android.text.format.DateUtils
import android.text.format.DateUtils.*
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.presentation.util.KotlinEpoxyHolder
import java.util.concurrent.TimeUnit

@EpoxyModelClass(layout = R.layout.item_post)
abstract class PostEpoxyModel : EpoxyModelWithHolder<PostHolder>() {
    @EpoxyAttribute
    lateinit var post: Post

    companion object {
        private const val MONTH_IN_MILLIS: Long = WEEK_IN_MILLIS * 4
    }

    override fun bind(holder: PostHolder) {
//        holder.imageView.setImageURI(imageUrl)
        post.apply {
            holder.postTitle.text = title
            holder.postText.text = text
            holder.postedByAndAt.text = "Posted by u/${author.username} • ${fromatTime(created)}"
            holder.postScore.text = score.toString()
            holder.postCommentCount.text = commentCount.toString()
            holder.subredditName.text = "r/${subreddit.name}"
        }
    }

    //TODO extract this method and unit test it. Maybe make an extension method with a name toTimeStamp
    private fun fromatTime(created: Long): String {
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
    val postTitle by bind<TextView>(R.id.postTitle)
    val postText by bind<TextView>(R.id.postText)
    val postScore by bind<TextView>(R.id.postScore)
    val postCommentCount by bind<TextView>(R.id.postCommentCount)
    val postedByAndAt by bind<TextView>(R.id.postedByAndAt)
    val subredditName by bind<TextView>(R.id.subredditName)
    val subredditImage by bind<ImageView>(R.id.subredditImage)
}

