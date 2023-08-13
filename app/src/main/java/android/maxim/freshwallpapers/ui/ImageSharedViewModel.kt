package android.maxim.freshwallpapers.ui

import android.app.Application
import android.content.SharedPreferences
import android.maxim.freshwallpapers.data.models.Image
import android.maxim.freshwallpapers.data.models.LikedImageMap
import android.maxim.freshwallpapers.data.repository.WallpapersRepository
import android.maxim.freshwallpapers.di.LikedImagesPrefs
import android.maxim.freshwallpapers.utils.LIKED
import android.maxim.freshwallpapers.utils.LIKED_IMAGE_MAP
import android.maxim.freshwallpapers.utils.LikedImageHelper
import androidx.lifecycle.*
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ImageSharedViewModel @Inject constructor(application: Application): AndroidViewModel(application) {

    @Inject
    lateinit var repository: WallpapersRepository
    @Inject
    @LikedImagesPrefs
    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var likedImageHelper: LikedImageHelper
    private lateinit var retrievedImageMap: LikedImageMap
    private val _imageList = MutableLiveData<List<Image>>()
    val imageList: LiveData<List<Image>> = _imageList

    suspend fun getCategoriesList() = liveData {
        repository.getCollectionsList().collect { value ->
            emit(value)
        }
    }

    fun getImageList(category: String) {
        if (category == LIKED) {
            _imageList.value = getLikedImageList()
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val response = repository.getImageList(category).body()?.imageList
                withContext(Dispatchers.Main) {
                    _imageList.value = response!!
                }
            }
        }
    }

    private fun getLikedImageList(): List<Image> {
        return getLikedImageMap()
            .likedImageMap
            .values
            .toList()
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

    fun getLikedImageMap(): LikedImageMap {
        if (!sharedPreferences.contains(LIKED_IMAGE_MAP)) {
            val likedImageMap = LikedImageMap(mutableMapOf())
            val likedImageMapGson = gson.toJson(likedImageMap)
            sharedPreferences
                .edit()
                .putString(LIKED_IMAGE_MAP, likedImageMapGson)
                .apply()
        }
        val serializedImageMap = sharedPreferences.getString(LIKED_IMAGE_MAP, null)
        if (serializedImageMap != null) {
            retrievedImageMap = gson.fromJson(serializedImageMap, LikedImageMap::class.java)
        } else {
            //TODO handle the error
        }
        return retrievedImageMap
    }
}