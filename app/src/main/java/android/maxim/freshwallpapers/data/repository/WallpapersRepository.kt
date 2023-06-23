package android.maxim.freshwallpapers.data.repository

import android.maxim.freshwallpapers.data.models.CategoriesList
import android.maxim.freshwallpapers.data.network.WallpapersApi
import android.maxim.freshwallpapers.utils.Constants
import android.util.Log
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import android.content.Context
import android.maxim.freshwallpapers.di.WallpapersRepositoryEntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.android.qualifiers.ApplicationContext

class WallpapersRepository @Inject constructor(@ApplicationContext context: Context) {

    private val hiltEntryPoint = EntryPoints.get(context.applicationContext, WallpapersRepositoryEntryPoint::class.java)
    private val wallpapersApi = hiltEntryPoint.wallpapersApi()
    private val categoriesList = hiltEntryPoint.categoriesList()

    fun getCategoriesList(): List<String> {
        Log.d(Constants.TAG, "WallpapersRepository.getCategoriesList()")
        val categoriesList = CategoriesList()
        //TODO():inject categoriesList
        return categoriesList.categoriesList
    }

    /*fun getCategoriesList() {
        wallpapersApi.getCategoriesList(Constants.PIXABAY_API_KEY, "").enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })
    }*/
}
