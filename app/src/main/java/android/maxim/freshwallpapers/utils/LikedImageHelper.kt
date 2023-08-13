package android.maxim.freshwallpapers.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.maxim.freshwallpapers.data.models.Image
import android.maxim.freshwallpapers.data.models.LikedImageMap
import com.google.gson.Gson
import javax.inject.Inject

class LikedImageHelper @Inject constructor(appContext: Context) {

    val sharedPreferences: SharedPreferences = appContext.getSharedPreferences(
        LIKED_IMAGE_PREFS,
        Application.MODE_PRIVATE
    )
    private val gson = Gson()
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