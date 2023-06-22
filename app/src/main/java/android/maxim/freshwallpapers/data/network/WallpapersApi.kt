package android.maxim.freshwallpapers.data.network

import android.maxim.freshwallpapers.data.models.CategoriesList
import android.maxim.freshwallpapers.data.models.Category
import retrofit2.http.POST
import retrofit2.http.Query

interface WallpapersApi {

    @POST("")
fun getCategoriesList(@Query("api_key") api_key: String): CategoriesList<Category>

}