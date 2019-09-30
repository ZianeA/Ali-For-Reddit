package com.visualeap.aliforreddit.presentation.main.frontPage

import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.presentation.util.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_post)
abstract class PostEpoxyModel : EpoxyModelWithHolder<PostHolder>() {
    //    @EpoxyAttribute
//    lateinit var title: String
//    @EpoxyAttribute
//    lateinit var text: String
//    @EpoxyAttribute
//    lateinit var authorName: String
//    @EpoxyAttribute
//    lateinit var score: String
//    @EpoxyAttribute
//    lateinit var commentCount: String
//    @EpoxyAttribute lateinit var imageUrl: Uri
    @EpoxyAttribute
    lateinit var post: Post

    override fun bind(holder: PostHolder) {
//        holder.imageView.setImageURI(imageUrl)
        post.apply {
            holder.postTitle.text = title
            holder.postText.text = text
            holder.postedByAndAt.text = author.username
            holder.postScore.text = score.toString()
            holder.postCommentCount.text = commentCount.toString()
        }
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

