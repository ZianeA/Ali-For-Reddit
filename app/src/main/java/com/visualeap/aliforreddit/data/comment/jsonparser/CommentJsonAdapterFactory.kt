package com.visualeap.aliforreddit.data.comment.jsonparser

import com.squareup.moshi.*
import com.visualeap.aliforreddit.data.comment.CommentResponse
import java.lang.reflect.Type

class CommentJsonAdapterFactory : JsonAdapter.Factory {
    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        val rawType = Types.getRawType(type)
        val commentListType =
            Types.newParameterizedType(List::class.java, CommentResponse.Comment::class.java)

        return when {
            rawType == CommentResponse::class.java -> {
                val adapter: JsonAdapter<List<CommentResponse.Comment>> =
                    moshi.adapter(commentListType)
                CommentResponseJsonAdapter(
                    adapter
                )
            }
            type == commentListType -> {
                val adapter: JsonAdapter<CommentResponse.Comment> =
                    moshi.adapter(CommentResponse.Comment::class.javaObjectType)
                CommentListingJsonAdapter(
                    adapter
                )
            }
            else -> null
        }
    }
}