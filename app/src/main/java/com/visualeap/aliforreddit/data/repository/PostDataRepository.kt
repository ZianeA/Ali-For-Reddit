package com.visualeap.aliforreddit.data.repository

import com.visualeap.aliforreddit.data.network.RedditService
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.repository.PostRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostDataRepository @Inject constructor(private val redditService: RedditService) :
    PostRepository {

    override fun getPosts(): Single<List<Post>> {
        //TODO remove this
//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(Interceptor {
//                Log.i(PostDataRepository::class.java.simpleName, "Hello from interceptor")
//                val response = it.proceed(it.request())
////                response.newBuilder().code(HttpURLConnection.HTTP_FORBIDDEN).build()
//                response
//            })
//            .addInterceptor(interceptor)
//            .authenticator(authenticator)
//            .build()
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://oauth.reddit.com/")
//            .addConverterFactory(MoshiConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .client(okHttpClient)
//            .build()

        return redditService.getPosts()
//        return Single.just(emptyList())
    }
}