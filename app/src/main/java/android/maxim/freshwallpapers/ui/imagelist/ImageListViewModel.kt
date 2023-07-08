package android.maxim.freshwallpapers.ui.imagelist

import android.maxim.freshwallpapers.data.models.Image
import android.maxim.freshwallpapers.data.models.ImageList
import android.maxim.freshwallpapers.data.repository.WallpapersRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ImageListViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var repository: WallpapersRepository

    private val _imageList = MutableLiveData<List<Image>>()
    val imageList: LiveData<List<Image>>
        get() = _imageList

    fun getImageList(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getImageList(category).body()?.imageList
            withContext(Dispatchers.Main) {
                _imageList.value = response!!
            }
        }
    }
}
