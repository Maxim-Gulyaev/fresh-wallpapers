package android.maxim.freshwallpapers.ui.imageslist

import android.maxim.freshwallpapers.data.repository.WallpapersRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageListViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var repository: WallpapersRepository

    fun testRequest(category: String) {
        viewModelScope.launch {
            repository.getImageList(category)
        }
    }
}