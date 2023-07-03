package android.maxim.freshwallpapers.data.network

import android.maxim.freshwallpapers.data.models.Image
import android.maxim.freshwallpapers.data.models.ImageList
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WallpapersApi {

    @GET(".")
    fun getCategoriesList(
        @Query("key") api_key: String,
        @Query("category") category: String
    ): Call<ResponseBody>

    /*@GET(".")
    fun getImageList(
        @Query("key") api_key: String,
        @Query("category") category: String
    ): Call<ResponseBody>*/

    @GET(".")
    suspend fun getImageList(
        @Query("key") api_key: String,
        @Query("category") category: String
    ): Response<ImageList>

}