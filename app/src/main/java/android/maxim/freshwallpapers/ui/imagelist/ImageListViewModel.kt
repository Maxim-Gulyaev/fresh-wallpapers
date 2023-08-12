package android.maxim.freshwallpapers.ui.imagelist

import android.content.SharedPreferences
import android.maxim.freshwallpapers.data.models.Image
import android.maxim.freshwallpapers.data.models.LikedImageMap
import android.maxim.freshwallpapers.data.repository.WallpapersRepository
import android.maxim.freshwallpapers.di.LikedImagesPrefs
import android.maxim.freshwallpapers.utils.LIKED
import android.maxim.freshwallpapers.utils.LIKED_IMAGE_MAP
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ImageListViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var repository: WallpapersRepository
    @Inject
    @LikedImagesPrefs
    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var gson: Gson
    private lateinit var retrievedImageMap: LikedImageMap
    private val _imageList = MutableLiveData<List<Image>>()
    val imageList: LiveData<List<Image>>
        get() = _imageList

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

        //TODO it duplicates code in image fragment
        if (!sharedPreferences.contains(LIKED_IMAGE_MAP)) {
            val likedImageMap = LikedImageMap(mutableMapOf())
            val likedImageMapGson = gson.toJson(likedImageMap)
            sharedPreferences
                .edit()
                .putString(LIKED_IMAGE_MAP, likedImageMapGson)
                .apply()
        }

        //TODO it duplicates code in image fragment
        val serializedImageMap = sharedPreferences.getString(LIKED_IMAGE_MAP, null)
        if (serializedImageMap != null) {
            retrievedImageMap = gson.fromJson(serializedImageMap, LikedImageMap::class.java)
        } else {
            //TODO handle the error
        }

        return retrievedImageMap.likedImageMap.values.toList()
    }
}
