package android.maxim.freshwallpapers.ui.imagelist

import android.app.Application
import android.content.SharedPreferences
import android.maxim.freshwallpapers.data.models.Image
import android.maxim.freshwallpapers.data.repository.WallpapersRepository
import android.maxim.freshwallpapers.di.LikedImagesPrefs
import android.maxim.freshwallpapers.utils.LIKED
import android.maxim.freshwallpapers.utils.LikedImageHelper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ImageListViewModel @Inject constructor(application: Application)
    : AndroidViewModel(application) {

    @Inject
    lateinit var repository: WallpapersRepository
    @Inject
    @LikedImagesPrefs
    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var gson: Gson
    private val likedImageHelper = LikedImageHelper(application)
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
        return likedImageHelper
            .getLikedImageMap()
            .likedImageMap
            .values
            .toList()
    }
}
