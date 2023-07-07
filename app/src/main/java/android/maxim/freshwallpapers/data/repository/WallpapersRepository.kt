package android.maxim.freshwallpapers.data.repository

import android.content.Context
import android.maxim.freshwallpapers.di.WallpapersRepositoryEntryPoint
import android.maxim.freshwallpapers.utils.Constants
import android.maxim.freshwallpapers.utils.Constants.TAG
import android.util.Log
import dagger.hilt.EntryPoints
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WallpapersRepository @Inject constructor(@ApplicationContext context: Context) {

    private val hiltEntryPoint = EntryPoints.get(
        context.applicationContext,
        WallpapersRepositoryEntryPoint::class.java)
    private val wallpapersApi = hiltEntryPoint.wallpapersApi()
    private var collectionsList = hiltEntryPoint.collectionsList()

    fun getCategoriesList(): List<String> {
        Log.d(TAG, "WallpapersRepository.getCategoriesList()")
        return collectionsList.collectionsList
    }

    suspend fun getImageList(collection: String) {
        Log.d(TAG, "WallpapersRepository.getImageList() with parameter $collection")
        val response = wallpapersApi.getImageList(
            Constants.PIXABAY_API_KEY,
            200,
            "background",
            "vertical",
            "photo",
            collection)
        if (response.isSuccessful) {
            Log.d(TAG, "WallpapersRepository.getImageList() coroutine response")
        }
    }
}
