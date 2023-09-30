package com.maxim.freshwallpapers.data.network

import com.maxim.freshwallpapers.data.models.ImageList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WallpapersApi {

    @GET("api/")
    suspend fun getImageList(
        @Query("key") apiKey: String,
        @Query("per_page") perPage: Int,
        @Query("image_type") imageType: String,
        @Query("safesearch") safeSearch: Boolean,
        @Query("q") collection: String
    ): Response<ImageList>

}