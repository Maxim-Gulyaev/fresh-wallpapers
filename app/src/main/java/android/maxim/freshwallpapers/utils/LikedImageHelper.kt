package android.maxim.freshwallpapers.utils

import android.content.Context
import android.maxim.freshwallpapers.data.models.Image
import android.maxim.freshwallpapers.data.models.LikedImageMap
import android.maxim.freshwallpapers.di.LikedImageHelperEntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LikedImageHelper @Inject constructor(@ApplicationContext appContext: Context) {

    private val hiltEntryPoint = EntryPoints.get(
        appContext.applicationContext,
        LikedImageHelperEntryPoint::class.java
    )
    private val sharedPreferences = hiltEntryPoint.sharedPreferences()
    private val gson = hiltEntryPoint.gson()
    private lateinit var retrievedImageMap: LikedImageMap
    private var serializedImageMap: String? = null

    fun getLikedImageMap(): LikedImageMap {
        if (!sharedPreferences.contains(LIKED_IMAGE_MAP)) {
            val likedImageMap = LikedImageMap(mutableMapOf())
            val likedImageMapGson = gson.toJson(likedImageMap)
            sharedPreferences
                .edit()
                .putString(LIKED_IMAGE_MAP, likedImageMapGson)
                .apply()
        }
        serializedImageMap = sharedPreferences.getString(LIKED_IMAGE_MAP, null)
        if (serializedImageMap != null) {
            retrievedImageMap = gson.fromJson(serializedImageMap, LikedImageMap::class.java)
        } else {
            //TODO handle the error
        }
        return retrievedImageMap
    }

    fun addImageToLiked(image: Image) {
        getLikedImageMap().likedImageMap.put(image.id, image)
        val imageListGson = gson.toJson(retrievedImageMap)
        sharedPreferences
            .edit()
            .putString(LIKED_IMAGE_MAP, imageListGson)
            .apply()
    }

    fun removeImageFromLiked(image: Image) {
        getLikedImageMap().likedImageMap.remove(image.id)
        val imageListGson = gson.toJson(retrievedImageMap)
        sharedPreferences
            .edit()
            .putString(LIKED_IMAGE_MAP, imageListGson)
            .apply()
    }
}