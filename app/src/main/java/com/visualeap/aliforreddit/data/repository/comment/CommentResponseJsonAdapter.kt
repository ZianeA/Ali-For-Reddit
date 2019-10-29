package com.visualeap.aliforreddit.data.repository.comment

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

class CommentResponseJsonAdapter(private val commentListAdapter: JsonAdapter<List<CommentResponse.Comment>>) :
    JsonAdapter<CommentResponse>() {
    override fun fromJson(reader: JsonReader): CommentResponse? {
        reader.beginArray()
        reader.skipValue()
        val comments = commentListAdapter.fromJson(reader)
        reader.endArray()

        return CommentResponse(comments!!)
    }

    override fun toJson(writer: JsonWriter, value: CommentResponse?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}