package com.meepo.feed

import com.meepo.config.http.ResponseWrapper
import com.meepo.feed.schema.*
import com.meepo.sdk.http.RetrofitManager
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @author  佟旭
 * @wechat tongxury
 * @date  2020/10/27 11:11 PM
 * @version 1.0
 */
interface Service {


    companion object {
        private var sInstance: Service? = null


        fun get(): Service {
            if (sInstance == null)
                sInstance = RetrofitManager.create(
                    Service::class.java
                )
            return sInstance as Service
        }
    }


    @GET("/api/v1/feed/comments")
    fun fetchComments(
        @Query("targetId") targetId: String,
        @Query("targetType") targetType: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Observable<ResponseWrapper<CommentResult>>

    @POST("/api/v1/feed/comments")
    fun addComment(@Body comment: ReqComment): Observable<ResponseWrapper<Comment>>

    @POST("/api/v1/feed/comment-stars")
    fun starComment(@Query("id") id: String): Observable<ResponseWrapper<Unit>>

    @GET("/api/v1/feed/items")
    fun fetch(@Query("category") category: String): Observable<ResponseWrapper<FeedResult>>

    @GET("/api/v1/feed/mini-videos")
    fun fetchMiniVideos(@Query("category") category: String): Observable<ResponseWrapper<FeedResult>>

    @GET("/api/v1/feed/short-videos")
    fun fetchShortVideos(@Query("category") category: String): Observable<ResponseWrapper<FeedResult>>

    @GET("/api/v1/feed/categories")
    fun fetchCategories(): Observable<ResponseWrapper<ArrayList<Category>>>

    @GET("/api/v1/feed/items/{id}")
    fun fetchDetail(@Path("id") id: String): Observable<ResponseWrapper<Item>>


    @GET("/api/v1/search/keywords")
    fun fetchSearchMetadata(): Observable<ResponseWrapper<SearchMetadata>>

    @GET("/api/v1/search/items")
    fun fetchByKeyword(@Query("keyword") keyword: String): Observable<ResponseWrapper<FeedResult>>

    @GET("/api/v1/history/user-items")
    fun fetchUserItems(@Query("itemType") itemType: String, @Query("page") page: Int): Observable<ResponseWrapper<FeedResult>>

    @POST("/api/v1/history/user-items")
    fun saveUserItem(
        @Query("itemId") itemId: String,
        @Query("itemType") itemType: String,
        @Query("action") action: Int
    ): Observable<ResponseWrapper<Item>>


}