package android.maxim.freshwallpapers.data.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WallpapersApi {

    @GET(".")
    fun getCategoriesList(
        @Query("key") api_key: String,
        @Query("category") category: String
    ): Call<ResponseBody>

}