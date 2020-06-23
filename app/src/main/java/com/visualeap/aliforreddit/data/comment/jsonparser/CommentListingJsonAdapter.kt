package com.visualeap.aliforreddit.data.comment.jsonparser

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.visualeap.aliforreddit.data.comment.CommentResponse.*

class CommentListingJsonAdapter(private val commentAdapter: JsonAdapter<Comment>) :
    JsonAdapter<List<Comment>>() {
    override fun fromJson(reader: JsonReader): List<Comment>? {
        val comments = mutableListOf<Comment>()

        // When a comment has no replies, the replies field is going to be an empty string.
        // If we try to parse it we get the following exception: "Expected BEGIN_OBJECT but was STRING"
        if(reader.peek() == JsonReader.Token.STRING){
            reader.skipValue()
            return null
        }

        reader.beginObject()
        while (reader.hasNext()) {
            if (reader.nextName() == "data") {
                reader.beginObject()
                while (reader.hasNext()) {
                    if (reader.nextName() == "children") {
                        reader.beginArray()
                        while (reader.hasNext()) {
                            reader.beginObject()
                            while (reader.hasNext()) {
                                when (reader.selectName(CHILDREN_OPTIONS)) {
                                    0 -> {
                                        val kind = reader.nextString()
                                        if (kind == "more") {
                                            reader.skipName()
                                            reader.skipValue()
                                        }
                                    }
                                    1 -> comments.add(commentAdapter.fromJson(reader)!!)
                                    -1 -> {
                                        reader.skipName()
                                        reader.skipValue()
                                    }
                                }
                            }
                            reader.endObject()
                        }
                        reader.endArray()
                    } else {
                        reader.skipValue()
                    }
                }
                reader.endObject()
            } else {
                reader.skipValue()
            }
        }
        reader.endObject()

        return comments
    }

    override fun toJson(writer: JsonWriter, value: List<Comment>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private val CHILDREN_OPTIONS = JsonReader.Options.of("kind", "data")
    }
}