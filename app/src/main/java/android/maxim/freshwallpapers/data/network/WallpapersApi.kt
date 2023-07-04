package android.maxim.freshwallpapers.data.network

import android.maxim.freshwallpapers.data.models.ImageList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WallpapersApi {

    @GET(".")
    suspend fun getImageList(
        @Query("key") apiKey: String,
        @Query("per_page") perPage: Int,
        @Query("category") backgroundCategory: String,
        @Query("orientation") orientation: String,
        @Query("image_type") imageType: String,
        @Query("q") category: String
    ): Response<ImageList>

}