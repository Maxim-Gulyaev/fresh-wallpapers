package android.maxim.freshwallpapers.data.network

import android.maxim.freshwallpapers.data.models.ImageList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WallpapersApi {

    @GET(".")
    suspend fun getImageList(
        @Query("key") api_key: String,
        @Query("category") category: String
    ): Response<ImageList>

}