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
import android.maxim.freshwallpapers.utils.Constants.TAG
import dagger.hilt.EntryPoints
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WallpapersRepository @Inject constructor(@ApplicationContext context: Context) {

    private val hiltEntryPoint = EntryPoints.get(context.applicationContext, WallpapersRepositoryEntryPoint::class.java)
    private val wallpapersApi = hiltEntryPoint.wallpapersApi()
    private val categoriesList = hiltEntryPoint.categoriesList()

    fun getCategoriesList(): List<String> {
        Log.d(TAG, "WallpapersRepository.getCategoriesList()")
        val categoriesList = CategoriesList()
        //TODO():inject categoriesList
        return categoriesList.categoriesList
    }

    suspend fun getImageList(category: String) {
        Log.d(TAG, "WallpapersRepository.getImageList() with parameter $category")
        val response = wallpapersApi.getImageList(Constants.PIXABAY_API_KEY, category)
        if (response.isSuccessful) {
            Log.d(TAG, "WallpapersRepository.getImageList() coroutine response")
        }
    }
}
