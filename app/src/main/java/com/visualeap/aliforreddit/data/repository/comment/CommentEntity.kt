package com.visualeap.aliforreddit.data.repository.comment

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.visualeap.aliforreddit.data.repository.post.PostEntity
import com.visualeap.aliforreddit.domain.model.Comment

//TODO add self reference
@Entity(
    foreignKeys = [ForeignKey(
        entity = PostEntity::class,
        parentColumns = ["id"],
        childColumns = ["postId"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class CommentEntity(
    @PrimaryKey val id: String,
    val authorName: String,
    val text: String,
    val score: Int,
    val creationDate: Long,
    val depth: Int,
    val postId: String,
    val parentId: String?
)
